package com.around_team.todolist.ui.common.enums

import androidx.compose.runtime.Composable
import com.around_team.todolist.R

enum class TodoPriority {
    Low, Medium, High;
}

fun TodoPriority.getIconId(): Int? {
    return when (this) {
        TodoPriority.Low -> R.drawable.ic_low_priority
        TodoPriority.Medium -> null
        TodoPriority.High -> R.drawable.ic_high_priority
    }
}