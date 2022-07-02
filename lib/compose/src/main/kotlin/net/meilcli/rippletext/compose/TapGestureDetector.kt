/**
 * Original Source: https://androidx.tech/artifacts/compose.foundation/foundation/1.1.1-source/androidx/compose/foundation/gestures/TapGestureDetector.kt.html
 */
package net.meilcli.rippletext.compose

import androidx.compose.foundation.gestures.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.unit.Density
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

internal suspend fun PointerInputScope.detectTapAndPress(
    onPress: suspend PressGestureScope.(Offset) -> Unit,
    onTap: (Offset) -> Unit
) {
    val pressScope = PressGestureScopeImpl(this)
    forEachGesture {
        coroutineScope {
            pressScope.reset()
            awaitPointerEventScope {
                val down = awaitFirstDown().also { it.consume() }
                launch { pressScope.onPress(down.position) }

                val up = waitForUpOrCancellation()
                if (up == null) {
                    pressScope.cancel()
                } else {
                    up.consume()
                    pressScope.release()
                    onTap(up.position)
                }
            }
        }
    }
}

private class PressGestureScopeImpl(
    density: Density
) : PressGestureScope, Density by density {

    private var isReleased = false
    private var isCanceled = false
    private val mutex = Mutex(locked = false)

    fun cancel() {
        isCanceled = true
        mutex.unlock()
    }

    fun release() {
        isReleased = true
        mutex.unlock()
    }

    fun reset() {
        mutex.tryLock()
        isReleased = false
        isCanceled = false
    }

    override suspend fun awaitRelease() {
        if (tryAwaitRelease().not()) {
            throw GestureCancellationException("The press gesture was canceled.")
        }
    }

    override suspend fun tryAwaitRelease(): Boolean {
        if (isReleased.not() && isCanceled.not()) {
            mutex.lock()
        }
        return isReleased
    }
}