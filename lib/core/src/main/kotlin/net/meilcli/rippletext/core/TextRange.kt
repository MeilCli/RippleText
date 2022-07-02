package net.meilcli.rippletext.core

@JvmInline
value class TextRange(private val value: IntRange) : Iterable<Int> {

    val start: Int
        get() = value.first

    val end: Int
        get() = value.last

    override fun iterator(): Iterator<Int> {
        return value.iterator()
    }

    override fun toString(): String {
        return value.toString()
    }
}