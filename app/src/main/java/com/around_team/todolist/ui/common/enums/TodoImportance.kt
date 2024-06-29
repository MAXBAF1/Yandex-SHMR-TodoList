package com.around_team.todolist.ui.common.enums

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.JetTodoListTheme

enum class TodoImportance(val text: Int? = null, val iconId: Int? = null) {
    Low(iconId = R.drawable.ic_low_priority),
    Basic(text = R.string.no),
    Important(iconId = R.drawable.ic_high_priority);

    companion object {
        fun getFromOrdinal(ordinal: Int): TodoImportance {
            return when (ordinal) {
                0 -> Low
                1 -> Basic
                2 -> Important
                else -> throw IllegalArgumentException(ordinal.toString())
            }
        }
    }
}


@Composable
fun TodoImportance.getIconColor(): Color {
    return when (this) {
        TodoImportance.Low -> JetTodoListTheme.colors.colors.gray
        TodoImportance.Basic -> Color.Transparent
        TodoImportance.Important -> JetTodoListTheme.colors.colors.red
    }
}