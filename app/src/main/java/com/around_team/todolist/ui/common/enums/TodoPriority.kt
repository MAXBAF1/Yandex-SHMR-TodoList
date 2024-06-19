package com.around_team.todolist.ui.common.enums

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.JetTodoListTheme

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

@Composable
fun TodoPriority.getIconColor(): Color? {
    return when (this) {
        TodoPriority.Low -> JetTodoListTheme.colors.colors.gray
        TodoPriority.Medium -> null
        TodoPriority.High -> JetTodoListTheme.colors.colors.red
    }
}