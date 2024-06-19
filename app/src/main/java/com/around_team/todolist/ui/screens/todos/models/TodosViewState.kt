package com.around_team.todolist.ui.screens.todos.models

import com.around_team.todolist.ui.common.enums.TodoPriority
import com.around_team.todolist.ui.common.models.TodoItem

data class TodosViewState(
    val todos: List<TodoItem> = listOf(),
    val completeCnt: Int = 0,
    val completedShowed: Boolean = false
)