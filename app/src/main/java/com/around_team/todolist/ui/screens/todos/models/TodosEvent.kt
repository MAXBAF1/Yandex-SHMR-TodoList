package com.around_team.todolist.ui.screens.todos.models

import com.around_team.todolist.ui.common.enums.NetworkConnectionState

/**
 * Sealed class representing events related to Todos functionality.
 */
sealed class TodosEvent {
    data class DeleteTodo(val id: String) : TodosEvent()
    data object ClickShowCompletedTodos : TodosEvent()
    data class CompleteTodo(val id: String): TodosEvent()
    data object CancelJobs : TodosEvent()
    data object HandleSnackbarActionClick : TodosEvent()
    data object HideSnackbar : TodosEvent()
    data object ClearMessage : TodosEvent()
    data object RefreshTodos : TodosEvent()
    data class HandleNetworkState(val networkConnectionState: NetworkConnectionState) : TodosEvent()
}