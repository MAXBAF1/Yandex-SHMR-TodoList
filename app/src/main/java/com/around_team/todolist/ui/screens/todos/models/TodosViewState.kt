package com.around_team.todolist.ui.screens.todos.models

import com.around_team.todolist.data.model.TodoItem

data class TodosViewState(
    val todos: List<TodoItem> = listOf(),
    val completeCnt: Int = 0,
    val completedShowed: Boolean = false,
    val exception: Boolean = true,
    val error: String? = null,
    val refreshing: Boolean = false
)