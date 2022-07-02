package net.meilcli.rippletext.app

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.inSpans
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.meilcli.rippletext.RippleTextView
import net.meilcli.rippletext.core.RippleClickableSpan
import net.meilcli.rippletext.core.SpanRange

class RecyclerViewActivity : AppCompatActivity() {

    private inner class TapSpan : RippleClickableSpan() {

        override fun onClick(widget: TextView, clickedSpanRange: SpanRange) {
            super.onClick(widget, clickedSpanRange)
            val clickedText = widget.text.slice(clickedSpanRange.toTextRange())
            showToast("Clicked: $clickedText ${clickedSpanRange.start}~${clickedSpanRange.end}")
        }
    }

    private class Adapter(
        private val items: List<CharSequence>
    ) : RecyclerView.Adapter<ViewHolder>() {

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder.create(parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }
    }

    private class ViewHolder(
        private val textView: RippleTextView
    ) : RecyclerView.ViewHolder(textView) {

        companion object {

            fun create(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
                val textView = view.findViewById<RippleTextView>(R.id.text)
                return ViewHolder(textView)
            }
        }

        fun bind(text: CharSequence) {
            textView.setText(text, TextView.BufferType.SPANNABLE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        val list = findViewById<RecyclerView>(R.id.list)
        list.adapter = Adapter((1..10).map { createText(it) })
        list.layoutManager = LinearLayoutManager(this)
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