package net.meilcli.rippletext.core

import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

open class RippleClickableSpan : ClickableSpan() {

    override fun onClick(widget: View) {

    }

    open fun onClick(widget: TextView, clickedSpanRange: SpanRange) {
        onClick(widget)
    }
}