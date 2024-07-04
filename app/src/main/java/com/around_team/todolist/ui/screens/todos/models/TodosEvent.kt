package com.around_team.todolist.ui.screens.todos.models

import androidx.compose.material3.SnackbarResult
import com.around_team.todolist.ui.common.enums.NetworkConnectionState

sealed class TodosEvent {
    data class DeleteTodo(val id: String) : TodosEvent()
    data object ClickShowCompletedTodos : TodosEvent()
    data class CompleteTodo(val id: String): TodosEvent()
    data object CancelJobs : TodosEvent()
    data class HandleSnackbarResult(val result: SnackbarResult) : TodosEvent()
    data object RefreshTodos : TodosEvent()
    data class HandleNetworkState(val networkConnectionState: NetworkConnectionState) : TodosEvent()

}