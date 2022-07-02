package net.meilcli.rippletext.app

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.inSpans
import net.meilcli.rippletext.RippleTextView
import net.meilcli.rippletext.core.RippleClickableSpan
import net.meilcli.rippletext.core.SpanRange


class RtlActivity : AppCompatActivity() {

    private inner class TapSpan : RippleClickableSpan() {

        override fun onClick(widget: TextView, clickedSpanRange: SpanRange) {
            super.onClick(widget, clickedSpanRange)
            val clickedText = widget.text.slice(clickedSpanRange.toTextRange())
            showToast("Clicked: $clickedText ${clickedSpanRange.start}~${clickedSpanRange.end}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rtl)

        val text = findViewById<RippleTextView>(R.id.text)
        text.setText(createText(), TextView.BufferType.SPANNABLE)
    }

    private fun createText(): CharSequence {
        val sb = SpannableStringBuilder()

        val addClickableText = {
            sb.inSpans(TapSpan()) {
                append("هذا النص قابل للنقر عليه وفعال.")
            }
        }
        val addNonClickableText = {
            sb.append("هذا النص غير قابل للنقر.")
        }
        for (i in 0 until 15) {
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