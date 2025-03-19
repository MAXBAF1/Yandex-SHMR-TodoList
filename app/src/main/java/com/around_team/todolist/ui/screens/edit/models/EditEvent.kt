package com.around_team.todolist.ui.screens.edit.models

import androidx.compose.ui.graphics.Color
import com.around_team.todolist.ui.common.enums.TodoImportance

/**
 * Sealed class representing events related to editing or creating a todo item.
 */
sealed class EditEvent {
    data class SetEditedTodo(val todoId: String?) : EditEvent()
    data class ChangeText(val text: String) : EditEvent()
    data class ChangePriority(val priority: TodoImportance) : EditEvent()
    data class ChangeColor(val color: Color) : EditEvent()
    data class ChangeDeadline(val date: Long) : EditEvent()
    data class SetCalendarShowState(val state: Boolean) : EditEvent()
    data object CheckDeadline : EditEvent()
    data object SaveTodo : EditEvent()
    data object DeleteTodo : EditEvent()
    data object ClearViewState : EditEvent()
}