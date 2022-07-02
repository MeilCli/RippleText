package net.meilcli.rippletext.emoji

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.DrawableRes
import androidx.core.content.withStyledAttributes
import androidx.emoji.widget.EmojiTextView
import net.meilcli.rippletext.core.RippleTextPresenter

@Suppress("LeakingThis")
open class RippleEmojiTextView : EmojiTextView {

    private val presenter = RippleTextPresenter(this, this::createDrawableState)

    constructor(context: Context) : super(context) {
        context.withStyledAttributes(attrs = R.styleable.RippleEmojiTextView) {
            val resourceId = getResourceId(R.styleable.RippleEmojiTextView_textRipple, -1)
            if (resourceId != -1) {
                presenter.setRippleDrawable(resourceId)
            }
        }
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        context.withStyledAttributes(set = attrs, attrs = R.styleable.RippleEmojiTextView) {
            val resourceId = getResourceId(R.styleable.RippleEmojiTextView_textRipple, -1)
            if (resourceId != -1) {
                presenter.setRippleDrawable(resourceId)
            }
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        context.withStyledAttributes(set = attrs, attrs = R.styleable.RippleEmojiTextView, defStyleAttr = defStyleAttr) {
            val resourceId = getResourceId(R.styleable.RippleEmojiTextView_textRipple, -1)
            if (resourceId != -1) {
                presenter.setRippleDrawable(resourceId)
            }
        }
    }

    init {
        presenter.initialize()
    }

    private fun createDrawableState(vararg additionalState: Int): IntArray {
        val baseState = onCreateDrawableState(additionalState.size)
        return mergeDrawableStates(baseState, additionalState)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        presenter.onMeasure()
    }

    override fun jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState()
        presenter.jumpDrawablesToCurrentState()
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || presenter.verifyDrawable(who)
    }

    override fun draw(canvas: Canvas?) {
        if (canvas != null) {
            presenter.draw(canvas)
        }
        super.draw(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }
        return presenter.onTouchEvent(event) || super.onTouchEvent(event)
    }

    fun setRippleDrawable(drawableCreator: () -> Drawable) {
        presenter.setRippleDrawable(drawableCreator)
    }

    fun setRippleDrawable(@DrawableRes drawableResource: Int) {
        presenter.setRippleDrawable(drawableResource)
    }
}