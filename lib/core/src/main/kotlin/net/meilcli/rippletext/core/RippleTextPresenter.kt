package net.meilcli.rippletext.core

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import net.meilcli.rippletext.core.internal.DrawablePool
import net.meilcli.rippletext.core.internal.RippleExtractor

class RippleTextPresenter(
    private val textView: TextView,
    private val createDrawableState: (IntArray) -> IntArray
) {

    companion object {

        // this value is referenced SDK
        private const val rippleDelayDuration = 225L
    }

    private var drawableCreator: () -> Drawable = {
        val outValue = TypedValue()
        textView.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        checkNotNull(ContextCompat.getDrawable(textView.context, outValue.resourceId))
    }
        set(value) {
            field = value
            drawablePool = DrawablePool(value)
        }

    private var drawablePool = DrawablePool(drawableCreator)
    private var ripples: List<Ripple> = emptyList()
    private var currentEventRipple: Ripple? = null
    private var touchDownUnixTime: Long = 0

    fun initialize() {
        textView.isClickable = true
        textView.isFocusable = true
    }

    fun onMeasure() {
        ripples = RippleExtractor(drawablePool).extract(
            textView.text,
            textView.layout,
            paddingTop = textView.paddingTop,
            paddingLeft = textView.paddingLeft
        )
    }

    fun jumpDrawablesToCurrentState() {
        for (ripple in ripples) {
            ripple.jumpDrawablesToCurrentState()
        }
    }

    fun verifyDrawable(who: Drawable): Boolean {
        return ripples.any { it.verifyDrawable(who) }
    }

    fun draw(canvas: Canvas) {
        for (ripple in ripples) {
            ripple.draw(canvas)
        }
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt() - textView.totalPaddingLeft + textView.scrollX
        val y = event.y.toInt() - textView.totalPaddingTop + textView.scrollY

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownUnixTime = System.currentTimeMillis()

                val ripple = ripples.find { it.contains(x, y) } ?: return false
                ripple.setHotspot(x, y)
                ripple.setDrawableState(
                    createDrawableState(
                        intArrayOf(
                            android.R.attr.state_enabled,
                            android.R.attr.state_pressed,
                            android.R.attr.state_focused
                        )
                    )
                )
                currentEventRipple = ripple
                textView.invalidate()
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                currentEventRipple?.setDrawableState(textView.drawableState)
                currentEventRipple = null
                textView.invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                if (currentEventRipple?.contains(x, y) == false) {
                    currentEventRipple?.setDrawableState(textView.drawableState)
                    currentEventRipple = null
                    textView.invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                val ripple = currentEventRipple
                if (ripple != null && ripple.contains(x, y)) {
                    currentEventRipple?.onClick(textView)

                    val delay = rippleDelayDuration - (System.currentTimeMillis() - touchDownUnixTime)
                    textView.postDelayed(
                        {
                            ripple.setDrawableState(textView.drawableState)
                            textView.invalidate()
                        },
                        delay
                    )
                }
                currentEventRipple = null
            }
        }

        return false
    }

    fun setRippleDrawable(drawableCreator: () -> Drawable) {
        this.drawableCreator = drawableCreator
        textView.requestLayout()
    }

    fun setRippleDrawable(@DrawableRes drawableResource: Int) {
        this.drawableCreator = {
            checkNotNull(ContextCompat.getDrawable(textView.context, drawableResource))
        }
        textView.requestLayout()
    }
}