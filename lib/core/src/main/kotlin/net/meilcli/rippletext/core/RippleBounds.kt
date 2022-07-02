package net.meilcli.rippletext.core

import android.graphics.Rect
import android.graphics.drawable.Drawable

class RippleBounds(
    private val bounds: Rect
) {

    fun contains(x: Int, y: Int): Boolean {
        return bounds.contains(x, y)
    }

    fun setBoundsToDrawable(drawable: Drawable, paddingTop: Int, paddingLeft: Int) {
        drawable.bounds = Rect(
            bounds.left + paddingLeft,
            bounds.top + paddingTop,
            bounds.right + paddingLeft,
            bounds.bottom + paddingTop
        )
    }

    fun setHotspotLeftToDrawable(drawable: Drawable) {
        drawable.setHotspot(bounds.left.toFloat(), bounds.exactCenterY())
    }

    fun setHotspotRightToDrawable(drawable: Drawable) {
        drawable.setHotspot(bounds.right.toFloat(), bounds.exactCenterY())
    }
}