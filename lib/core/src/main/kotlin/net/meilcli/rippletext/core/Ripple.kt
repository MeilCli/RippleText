package net.meilcli.rippletext.core

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.style.ClickableSpan
import android.widget.TextView

class Ripple(
    private val location: RippleLocation,
    private val span: ClickableSpan,
    private val drawers: List<RippleDrawer>
) {

    fun jumpDrawablesToCurrentState() {
        for (drawer in drawers) {
            drawer.jumpDrawableToCurrentState()
        }
    }

    fun verifyDrawable(who: Drawable): Boolean {
        return drawers.any { it.verifyDrawable(who) }
    }

    fun draw(canvas: Canvas) {
        for (drawer in drawers) {
            drawer.draw(canvas)
        }
    }

    fun contains(x: Int, y: Int): Boolean {
        return drawers.any { it.contains(x, y) }
    }

    fun setHotspot(x: Int, y: Int) {
        val pointerDrawerIndex = drawers.indexOfFirst { it.contains(x, y) }
        if (pointerDrawerIndex < 0) {
            return
        }

        val rightStartDrawers = drawers.slice(0 until pointerDrawerIndex)
        val pointerStartDrawer = drawers[pointerDrawerIndex]
        val leftStartDrawers = drawers.slice(pointerDrawerIndex + 1 until drawers.size)

        for (rightStartDrawer in rightStartDrawers) {
            rightStartDrawer.setHotspotRight()
        }
        pointerStartDrawer.setHotspot(x.toFloat(), y.toFloat())
        for (leftStartDrawer in leftStartDrawers) {
            leftStartDrawer.setHotspotLeft()
        }
    }

    fun setDrawableState(state: IntArray) {
        for (drawer in drawers) {
            drawer.setDrawableState(state)
        }
    }

    fun onClick(view: TextView) {
        if (span is RippleClickableSpan) {
            span.onClick(view, location.rangeInText)
        } else {
            span.onClick(view)
        }
    }
}