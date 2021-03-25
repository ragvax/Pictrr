package com.ragvax.picttr.utils

import kotlin.math.ln
import kotlin.math.pow

fun Int.toPrettyString(): String {
    if (this < 1000) return this.toString()
    val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()
    return String.format("%.1f%c", this / 1000.0.pow(exp.toDouble()), "KMBTPE"[exp - 1])
}