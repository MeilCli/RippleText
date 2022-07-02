package net.meilcli.rippletext.app

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.inSpans
import net.meilcli.rippletext.RippleTextView
import net.meilcli.rippletext.core.RippleClickableSpan
import net.meilcli.rippletext.core.SpanRange

class MainActivity : AppCompatActivity() {

    private inner class TapSpan(
        private val activityClass: Class<*>
    ) : RippleClickableSpan() {

        override fun onClick(widget: TextView, clickedSpanRange: SpanRange) {
            super.onClick(widget, clickedSpanRange)
            val clickedText = widget.text.slice(clickedSpanRange.toTextRange())
            showToast("Clicked: $clickedText ${clickedSpanRange.start}~${clickedSpanRange.end}")
            startActivity(Intent(applicationContext, activityClass))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text = findViewById<RippleTextView>(R.id.text)
        text.setText(createText(), TextView.BufferType.SPANNABLE)
    }

    private fun createText(): CharSequence {
        val sb = SpannableStringBuilder()

        sb.inSpans(RelativeSizeSpan(1.3f)) {
            appendLine("Simple RippleTextView sample")
        }
        sb.inSpans(TapSpan(SimpleActivity::class.java)) {
            appendLine("Go to sample")
        }

        sb.appendLine()

        sb.inSpans(RelativeSizeSpan(1.3f)) {
            appendLine("RippleTextView sample in scrollable view")
        }
        sb.inSpans(TapSpan(ScrollViewActivity::class.java)) {
            appendLine("Go to sample")
        }

        sb.appendLine()

        sb.inSpans(RelativeSizeSpan(1.3f)) {
            appendLine("RippleTextView sample in recycler view")
        }
        sb.inSpans(TapSpan(RecyclerViewActivity::class.java)) {
            appendLine("Go to sample")
        }

        sb.appendLine()

        sb.inSpans(RelativeSizeSpan(1.3f)) {
            appendLine("Rtl RippleTextView sample")
        }
        sb.inSpans(TapSpan(RtlActivity::class.java)) {
            appendLine("Go to sample")
        }

        sb.appendLine()

        sb.inSpans(RelativeSizeSpan(1.3f)) {
            appendLine("Custom drawable RippleTextView sample by code configuration")
        }
        sb.inSpans(TapSpan(CustomDrawable1Activity::class.java)) {
            appendLine("Go to sample")
        }

        sb.appendLine()

        sb.inSpans(RelativeSizeSpan(1.3f)) {
            appendLine("Custom drawable RippleTextView sample by xml configuration")
        }
        sb.inSpans(TapSpan(CustomDrawable2Activity::class.java)) {
            appendLine("Go to sample")
        }

        sb.appendLine()

        sb.inSpans(RelativeSizeSpan(1.3f)) {
            appendLine("Counter sample of RippleTextView")
        }
        sb.inSpans(TapSpan(CounterActivity::class.java)) {
            appendLine("Go to sample")
        }

        sb.appendLine()

        sb.inSpans(RelativeSizeSpan(1.3f)) {
            appendLine("Compose sample in android scrollable view")
        }
        sb.inSpans(TapSpan(Compose1Activity::class.java)) {
            appendLine("Go to sample")
        }

        sb.appendLine()

        sb.inSpans(RelativeSizeSpan(1.3f)) {
            appendLine("Compose sample of compose scrollable")
        }
        sb.inSpans(TapSpan(Compose2Activity::class.java)) {
            appendLine("Go to sample")
        }

        sb.appendLine()

        sb.inSpans(RelativeSizeSpan(1.3f)) {
            appendLine("Rtl Compose sample")
        }
        sb.inSpans(TapSpan(Compose3Activity::class.java)) {
            appendLine("Go to sample")
        }

        return sb
    }
}