package com.around_team.todolist.ui.screens.todos.models

import com.around_team.todolist.ui.common.enums.NetworkConnectionState
import com.around_team.todolist.ui.common.models.TodoItem

data class TodosViewState(
    val todos: List<TodoItem> = listOf(),
    val completeCnt: Int = 0,
    val completedShowed: Boolean = false,
    val exception: Boolean = true,
    val errorId: Int? = null,
    val refreshing: Boolean = false,
    val connectionState: NetworkConnectionState = NetworkConnectionState.Available
)