package net.meilcli.rippletext.app

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.inSpans
import net.meilcli.rippletext.RippleTextView
import net.meilcli.rippletext.core.RippleClickableSpan
import net.meilcli.rippletext.core.SpanRange

class CustomDrawable1Activity : AppCompatActivity() {

    private inner class TapSpan : RippleClickableSpan() {

        override fun onClick(widget: TextView, clickedSpanRange: SpanRange) {
            super.onClick(widget, clickedSpanRange)
            val clickedText = widget.text.slice(clickedSpanRange.toTextRange())
            showToast("Clicked: $clickedText ${clickedSpanRange.start}~${clickedSpanRange.end}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_drawable1)

        val text = findViewById<RippleTextView>(R.id.text)
        text.setRippleDrawable(R.drawable.background_ripple)
        text.setText(createText(), TextView.BufferType.SPANNABLE)
    }

    private fun createText(): CharSequence {
        val sb = SpannableStringBuilder()

        val addClickableText = {
            sb.inSpans(TapSpan()) {
                append("This text is clickable and effective.")
            }
        }
        val addNonClickableText = {
            sb.append("This text is not clickable.")
        }
        for (i in 0 until 3) {
            for (j in 0..i) {
                addNonClickableText()
            }
            sb.append(' ')
            addClickableText()
            sb.appendLine()
        }

        return sb
    }
}