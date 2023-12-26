package com.javdc.tussapp.presentation.util

import android.content.Context
import com.javdc.tussapp.presentation.R
import kotlin.math.roundToInt

fun getFormattedTimeLeftString(context: Context, seconds: Int?): String {
    if (seconds == -1800) return context.getString(R.string.duration_more_than_30_min)
    if (seconds == null || seconds < 0) return context.getString(R.string.duration_unknown)

    val minutes = (seconds.toDouble() / 60).roundToInt()
    return context.getString(R.string.duration_format_minutes, minutes)
}
