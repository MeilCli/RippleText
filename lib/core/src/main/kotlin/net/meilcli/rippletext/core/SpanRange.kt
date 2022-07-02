package net.meilcli.rippletext.core

@JvmInline
value class SpanRange(private val value: IntRange) : Iterable<Int> {

    val start: Int
        get() = value.first

    val end: Int
        get() = value.last

    fun isValid(): Boolean {
        if (value.isEmpty()) {
            return false
        }
        if (value.first < 0 || value.last < 0) {
            return false
        }

        return true
    }

    override fun iterator(): Iterator<Int> {
        return value.iterator()
    }

    override fun toString(): String {
        return value.toString()
    }

    fun toTextRange(): TextRange {
        return TextRange(start until end)
    }
}