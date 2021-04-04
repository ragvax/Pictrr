package com.ragvax.picttr.utils

import android.content.Context
import android.util.TypedValue

fun Int.dpToPx(context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
).toInt()