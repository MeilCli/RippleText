package net.meilcli.rippletext.app

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.inSpans
import net.meilcli.rippletext.RippleTextView
import net.meilcli.rippletext.core.RippleClickableSpan
import net.meilcli.rippletext.core.SpanRange

class CounterActivity : AppCompatActivity() {

    private inner class TapSpan : RippleClickableSpan() {

        override fun onClick(widget: TextView, clickedSpanRange: SpanRange) {
            super.onClick(widget, clickedSpanRange)
            val clickedText = widget.text.slice(clickedSpanRange.toTextRange())
            showToast("Clicked: $clickedText ${clickedSpanRange.start}~${clickedSpanRange.end}")
        }
    }

    private var counter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        val text = findViewById<RippleTextView>(R.id.text)
        text.setText(createText(counter), TextView.BufferType.SPANNABLE)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            counter += 1
            text.setText(createText(counter), TextView.BufferType.SPANNABLE)
        }
    }

    private fun createText(count: Int): CharSequence {
        val sb = SpannableStringBuilder()

        val addClickableText = {
            sb.inSpans(TapSpan()) {
                append("This text is clickable and effective.")
            }
        }
        val addNonClickableText = {
            sb.append("This text is not clickable.")
        }
        for (i in 0 until count) {
            addNonClickableText()
            sb.append(' ')
            addClickableText()
            sb.appendLine()
        }

        return sb
    }
}