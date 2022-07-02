package net.meilcli.rippletext.core.internal

import android.graphics.Rect
import android.text.Layout
import android.text.Spannable
import android.text.style.ClickableSpan
import net.meilcli.rippletext.core.*
import net.meilcli.rippletext.core.internal.extensions.getRange
import kotlin.math.max
import kotlin.math.min

internal class RippleExtractor(
    private val drawablePool: DrawablePool
) {

    private class LineLocation(
        val line: Int,
        val rangeInText: SpanRange
    )

    fun extract(text: CharSequence, layout: Layout, paddingTop: Int, paddingLeft: Int): List<Ripple> {
        drawablePool.release()

        val result = mutableListOf<Ripple>()
        if (text !is Spannable) {
            return result
        }

        val spans = text.getSpans(0, text.length, ClickableSpan::class.java)
        for (span in spans) {
            val rangeInText = text.getRange(span)
            if (rangeInText.isValid().not()) {
                continue
            }
            val lineLocations = computeTextLine(layout, rangeInText).filter { it.rangeInText.isValid() }
            val bounds = lineLocations.map { location ->
                RippleBounds(
                    Rect(
                        layout.getLeft(location),
                        layout.getLineTop(location.line),
                        layout.getRight(location),
                        layout.getLineBottom(location.line)
                    )
                )
            }
            val rippleDrawers = bounds.map {
                RippleDrawer(
                    it,
                    drawablePool.rent()
                        .apply {
                            it.setBoundsToDrawable(this, paddingTop = paddingTop, paddingLeft = paddingLeft)
                        }
                )
            }
            result += Ripple(RippleLocation(rangeInText), span, rippleDrawers)
        }

        return result
    }

    private fun computeTextLine(layout: Layout, rangeInText: SpanRange): List<LineLocation> {
        val lineAndOffsets = rangeInText.map { Pair(layout.getLineForOffset(it), it) }
        val lines = lineAndOffsets.map { it.first }.distinct()
        return lines.map { line ->
            val offsets = lineAndOffsets.filter { it.first == line }.map { it.second }
            LineLocation(line, SpanRange(offsets.first()..offsets.last()))
        }
    }

    private fun Layout.getLeft(location: LineLocation): Int {
        val startX = getPrimaryHorizontal(location.rangeInText.start)
        val endX = getSecondaryHorizontal(location.rangeInText.end)
        return min(startX, endX).toInt()
    }

    private fun Layout.getRight(location: LineLocation): Int {
        val startX = getPrimaryHorizontal(location.rangeInText.start)
        val endX = getSecondaryHorizontal(location.rangeInText.end)
        return max(startX, endX).toInt()
    }
}