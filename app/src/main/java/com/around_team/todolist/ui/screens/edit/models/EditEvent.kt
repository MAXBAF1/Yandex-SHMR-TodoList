package com.around_team.todolist.ui.screens.edit.models

import com.around_team.todolist.ui.common.enums.TodoPriority

sealed class EditEvent {
    data class ChangeText(val text: String) : EditEvent()
    data class ChangePriority(val priority: TodoPriority) : EditEvent()
    data class ChangeDeadline(val date: Long) : EditEvent()
    data class SetCalendarShowState(val state: Boolean) : EditEvent()
    data object CheckDeadline : EditEvent()
}