package net.meilcli.rippletext.core

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable

class RippleDrawer(
    private val bounds: RippleBounds,
    private val drawable: Drawable
) {

    fun jumpDrawableToCurrentState() {
        drawable.jumpToCurrentState()
    }

    fun verifyDrawable(who: Drawable): Boolean {
        return drawable == who
    }

    fun draw(canvas: Canvas) {
        drawable.draw(canvas)
    }

    fun contains(x: Int, y: Int): Boolean {
        return bounds.contains(x, y)
    }

    fun setHotspot(x: Float, y: Float) {
        drawable.setHotspot(x, y)
    }

    fun setHotspotRight() {
        bounds.setHotspotRightToDrawable(drawable)
    }

    fun setHotspotLeft() {
        bounds.setHotspotLeftToDrawable(drawable)
    }

    fun setDrawableState(state: IntArray) {
        if (drawable.isStateful) {
            drawable.state = state
            jumpDrawableToCurrentState()
        }
    }
}