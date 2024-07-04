package com.around_team.todolist.ui.screens.edit.models

import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.ui.common.enums.TodoImportance
import java.util.Date
import java.util.UUID

data class EditViewState(
    val saveEnable: Boolean = false,
    val editedTodo: TodoItem = TodoItem(
        id = UUID.randomUUID().toString(),
        text = "",
        importance = TodoImportance.Basic,
        done = false,
        creationDate = Date().time
    ),
    val deadlineChecked: Boolean = false,
    val showCalendar: Boolean = true,
    val toTodosScreen: Boolean = false,
    val exception: Boolean = true,
)