package com.around_team.todolist.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimeInMillis(timeInMillis: Long, format: String = "d MMMM yyyy"): String {
    val dateFormat = SimpleDateFormat(format, Locale("ru"))
    val date = Date(timeInMillis)
    return dateFormat.format(date)
}