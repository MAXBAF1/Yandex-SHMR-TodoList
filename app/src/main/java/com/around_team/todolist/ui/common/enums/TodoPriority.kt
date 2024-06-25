package com.around_team.todolist.ui.common.enums

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.JetTodoListTheme

enum class TodoPriority(val text: Int? = null, val iconId: Int? = null) {
    Low(iconId = R.drawable.ic_low_priority),
    Medium(text = R.string.no),
    High(iconId = R.drawable.ic_high_priority);

    companion object {
        fun getFromOrdinal(ordinal: Int): TodoPriority {
            return when (ordinal) {
                0 -> Low
                1 -> Medium
                2 -> High
                else -> throw IllegalArgumentException(ordinal.toString())
            }
        }
    }
}


@Composable
fun TodoPriority.getIconColor(): Color {
    return when (this) {
        TodoPriority.Low -> JetTodoListTheme.colors.colors.gray
        TodoPriority.Medium -> Color.Transparent
        TodoPriority.High -> JetTodoListTheme.colors.colors.red
    }
}