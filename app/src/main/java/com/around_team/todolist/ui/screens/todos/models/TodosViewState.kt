package com.around_team.todolist.ui.screens.todos.models

import com.around_team.todolist.ui.common.enums.NetworkConnectionState
import com.around_team.todolist.ui.common.models.TodoItem

/**
 * Data class representing the state of Todos screen.
 *
 * @property todos List of todo items currently displayed.
 * @property completeCnt Count of completed todo items.
 * @property completedShowed Flag indicating if completed todos are currently visible.
 * @property exception Flag indicating if an exception occurred.
 * @property messageId Optional ID of a message related to the state.
 * @property refreshing Flag indicating if the screen is currently refreshing.
 * @property connectionState The current network connection state.
 */
data class TodosViewState(
    val todos: List<TodoItem> = listOf(),
    val completeCnt: Int = 0,
    val completedShowed: Boolean = false,
    val exception: Boolean = true,
    val messageId: Int? = null,
    val refreshing: Boolean = false,
    val connectionState: NetworkConnectionState = NetworkConnectionState.Available
)