package com.around_team.todolist.ui.screens.edit.models

import com.around_team.todolist.ui.common.enums.TodoPriority
import com.around_team.todolist.ui.common.models.TodoItem

sealed class EditEvent {
    data class SetEditedTodo(val todo: TodoItem?) : EditEvent()
    data class ChangeText(val text: String) : EditEvent()
    data class ChangePriority(val priority: TodoPriority) : EditEvent()
    data class ChangeDeadline(val date: Long) : EditEvent()
    data class SetCalendarShowState(val state: Boolean) : EditEvent()
    data object CheckDeadline : EditEvent()
    data object ClearViewState : EditEvent()
}