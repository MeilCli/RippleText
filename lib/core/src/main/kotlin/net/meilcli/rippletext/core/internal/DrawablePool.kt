package net.meilcli.rippletext.core.internal

import android.graphics.drawable.Drawable

internal class DrawablePool(
    private val drawableCreator: () -> Drawable
) {

    private val drawables = mutableListOf<Drawable>()
    private var rentCount = 0

    fun rent(): Drawable {
        if (rentCount < drawables.size) {
            val result = drawables[rentCount]
            rentCount += 1
            return result
        }
        val result = drawableCreator()
        drawables += result
        rentCount += 1
        return result
    }

    fun release() {
        rentCount = 0
    }
}