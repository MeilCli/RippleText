package net.meilcli.rippletext.core.internal.extensions

import android.text.Spannable
import net.meilcli.rippletext.core.SpanRange

internal fun Spannable.getRange(span: Any): SpanRange {
    return SpanRange(getSpanStart(span)..getSpanEnd(span))
}