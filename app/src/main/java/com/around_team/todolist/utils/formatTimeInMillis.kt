package com.around_team.todolist.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FormatTimeInMillis {
    private val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
    fun format(timeInMillis: Long, format: String = "d MMMM yyyy"): String {
        dateFormat.applyPattern(format)
        val date = Date(timeInMillis)
        return dateFormat.format(date)
    }
}

