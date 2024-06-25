package com.around_team.todolist.ui.screens.todos.models

import com.around_team.todolist.ui.common.enums.TodoPriority
import com.around_team.todolist.data.model.TodoItem
import com.around_team.todolist.ui.screens.edit.EditScreen

data class TodosViewState(
    val todos: List<TodoItem> = listOf(),
    val completeCnt: Int = 0,
    val completedShowed: Boolean = false,
)