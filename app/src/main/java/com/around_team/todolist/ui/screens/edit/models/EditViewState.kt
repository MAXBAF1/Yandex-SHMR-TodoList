package com.around_team.todolist.ui.screens.edit.models

import com.around_team.todolist.ui.common.enums.TodoPriority
import com.around_team.todolist.ui.common.models.TodoItem

data class EditViewState(
    val saveEnable: Boolean = false,
    val editedTodo: TodoItem = TodoItem("", "", TodoPriority.Medium, false, ""),
    val deadlineChecked: Boolean = false
)