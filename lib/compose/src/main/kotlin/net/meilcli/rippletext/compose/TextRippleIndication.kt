package net.meilcli.rippletext.compose

import android.graphics.RectF
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult

class TextRippleIndication(
    private val annotatedString: AnnotatedString,
    private val layoutResult: MutableState<TextLayoutResult?>
) : Indication {

    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        val rippleIndicationInstance = rememberRipple().rememberUpdatedInstance(interactionSource = interactionSource)
        val instance = remember(rippleIndicationInstance) {
            TextRippleIndicationInstance(rippleIndicationInstance)
        }
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect {
                when (it) {
                    is PressInteraction.Press -> {
                        val layoutResult = layoutResult.value
                        if (layoutResult != null) {
                            val position = layoutResult.getOffsetForPosition(it.pressPosition)
                            val annotations = annotatedString.getStringAnnotations(
                                "click",
                                start = position,
                                end = position
                            )
                            if (annotations.isNotEmpty()) {
                                val annotation = annotations.first()
                                instance.lineCount = layoutResult.lineCount
                                instance.rectMap = computePressedTextRectMap(
                                    layoutResult,
                                    annotation.start..annotation.end
                                )
                            } else {
                                instance.lineCount = 0
                                instance.rectMap = emptyMap()
                            }
                        }
                    }
                }
            }
        }
        return instance
    }

    private fun computePressedTextRectMap(layoutResult: TextLayoutResult, range: IntRange): Map<Int, RectF> {
        val lineAndOffsets = range.map { Pair(layoutResult.getLineForOffset(it), it) }
        val lines = lineAndOffsets.map { it.first }.distinct()
        return lines.associateWith { line ->
            val offsets = lineAndOffsets.filter { it.first == line }.map { it.second }
            val start = offsets.first()
            val end = offsets.last()
            val lineTop = layoutResult.getLineTop(line)
            val lineBottom = layoutResult.getLineBottom(line)
            val lineLeft = layoutResult.getHorizontalPosition(start, true)
            val lineRight = layoutResult.getHorizontalPosition(end, false)
            RectF(lineLeft, lineTop, lineRight, lineBottom)
        }
    }

    class TextRippleIndicationInstance(
        private val rippleIndicationInstance: IndicationInstance
    ) : IndicationInstance {

        var lineCount: Int = 0
        var rectMap: Map<Int, RectF> = emptyMap()

        private fun IndicationInstance.drawIndication(scope: ContentDrawScope) {
            scope.drawIndication()
        }

        override fun ContentDrawScope.drawIndication() {
            drawContent()
            // mark using indication to compose system
            clipRect(left = 0f, top = 0f, right = 0f, bottom = 0f) {
                rippleIndicationInstance.drawIndication(ClipContentDrawScope(this))
            }
            for (i in 0 until lineCount) {
                val rect = rectMap.getOrElse(i) { RectF() }
                clipRect(left = rect.left, top = rect.top, right = rect.right, bottom = rect.bottom) {
                    rippleIndicationInstance.drawIndication(ClipContentDrawScope(this))
                }
            }
        }
    }

    class ClipContentDrawScope(
        private val clippedDrawScope: DrawScope
    ) : DrawScope by clippedDrawScope, ContentDrawScope {

        override fun drawContent() {
        }
    }
}