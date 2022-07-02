/**
 * Original Source: https://androidx.tech/artifacts/compose.foundation/foundation/1.1.1-source/androidx/compose/foundation/Clickable.kt.html
 */
package net.meilcli.rippletext.compose

import android.view.ViewConfiguration
import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PressedInteractionSourceDisposableEffect(
    interactionSource: MutableInteractionSource,
    pressedInteraction: MutableState<PressInteraction.Press?>
) {
    DisposableEffect(interactionSource) {
        onDispose {
            pressedInteraction.value?.let { oldValue ->
                val interaction = PressInteraction.Cancel(oldValue)
                interactionSource.tryEmit(interaction)
                pressedInteraction.value = null
            }
        }
    }
}

internal suspend fun PressGestureScope.handlePressInteraction(
    pressPoint: Offset,
    interactionSource: MutableInteractionSource,
    pressedInteraction: MutableState<PressInteraction.Press?>,
    delayPressInteraction: State<() -> Boolean>
) {
    coroutineScope {
        val delayJob = launch {
            if (delayPressInteraction.value()) {
                delay(ViewConfiguration.getTapTimeout().toLong())
            }
            val pressInteraction = PressInteraction.Press(pressPoint)
            interactionSource.emit(pressInteraction)
            pressedInteraction.value = pressInteraction
        }
        val success = tryAwaitRelease()
        if (delayJob.isActive) {
            delayJob.cancelAndJoin()
            if (success) {
                val pressInteraction = PressInteraction.Press(pressPoint)
                val releaseInteraction = PressInteraction.Release(pressInteraction)
                interactionSource.emit(pressInteraction)
                interactionSource.emit(releaseInteraction)
            }
        } else {
            pressedInteraction.value?.let { pressInteraction ->
                val endInteraction = if (success) {
                    PressInteraction.Release(pressInteraction)
                } else {
                    PressInteraction.Cancel(pressInteraction)
                }
                interactionSource.emit(endInteraction)
            }
        }
        pressedInteraction.value = null
    }
}