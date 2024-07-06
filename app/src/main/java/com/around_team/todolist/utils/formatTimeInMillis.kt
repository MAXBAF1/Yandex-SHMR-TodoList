package com.around_team.todolist.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Singleton object responsible for formatting time in milliseconds into a string representation.
 */
object FormatTimeInMillis {

    private val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))

    /**
     * Formats the given time in milliseconds into a string using the specified format.
     *
     * @param timeInMillis The time to format, represented in milliseconds since January 1, 1970, 00:00:00 GMT.
     * @param format The format pattern to apply. Defaults to "d MMMM yyyy".
     * @return A formatted string representing the time in the specified format.
     */
    fun format(timeInMillis: Long, format: String = "d MMMM yyyy"): String {
        dateFormat.applyPattern(format)
        val date = Date(timeInMillis)
        return dateFormat.format(date)
    }
}