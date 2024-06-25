package com.around_team.todolist.ui.screens.edit.models

import com.around_team.todolist.ui.common.enums.TodoPriority
import com.around_team.todolist.data.model.TodoItem
import java.util.UUID

data class EditViewState(
    val saveEnable: Boolean = false,
    val editedTodo: TodoItem = TodoItem(UUID.randomUUID().toString(), "", TodoPriority.Medium, false, ""),
    val deadlineChecked: Boolean = false,
    val showCalendar: Boolean = true
)