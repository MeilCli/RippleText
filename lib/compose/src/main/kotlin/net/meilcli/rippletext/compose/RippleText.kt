package net.meilcli.rippletext.compose

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun RippleText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    onClick: (Int) -> Unit
) {
    // Original Source:
    // https://androidx.tech/artifacts/compose.foundation/foundation/1.1.1-source/androidx/compose/foundation/Clickable.kt.html
    // https://androidx.tech/artifacts/compose.foundation/foundation/1.1.1-source/androidx/compose/foundation/text/ClickableText.kt.html
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val onClickState = rememberUpdatedState(onClick)
    val indication = remember(text, layoutResult) { TextRippleIndication(text, layoutResult) }
    val interactionSource = remember { MutableInteractionSource() }
    val pressedInteraction = remember { mutableStateOf<PressInteraction.Press?>(null) }
    if (enabled) {
        PressedInteractionSourceDisposableEffect(interactionSource, pressedInteraction)
    }
    val isRootInScrollableContainer = isComposeRootInScrollableContainer()
    // FixMe: cannot write code because original source is internal
    //
    val isClickableInScrollableContainer = remember { mutableStateOf(true) }
    val delayPressInteraction = rememberUpdatedState {
        isClickableInScrollableContainer.value || isRootInScrollableContainer()
    }
    val pressIndicator = Modifier.pointerInput(interactionSource) {
        detectTapAndPress(
            onPress = {
                handlePressInteraction(
                    it,
                    interactionSource,
                    pressedInteraction,
                    delayPressInteraction
                )
            },
            onTap = { offset ->
                layoutResult.value?.let {
                    onClickState.value.invoke(it.getOffsetForPosition(offset))
                }
            }
        )
    }

    BasicText(
        text = text,
        modifier = modifier.then(pressIndicator.indication(interactionSource, indication)),
        style = style,
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
        onTextLayout = {
            layoutResult.value = it
            onTextLayout(it)
        }
    )
}