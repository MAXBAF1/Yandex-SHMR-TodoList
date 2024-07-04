package com.around_team.todolist.ui.screens.todos

import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.viewModelScope
import com.around_team.todolist.R
import com.around_team.todolist.data.network.repositories.Repository
import com.around_team.todolist.ui.common.enums.NetworkConnectionState
import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.ui.screens.todos.models.TodosEvent
import com.around_team.todolist.ui.screens.todos.models.TodosViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val repository: Repository,

    ) : BaseViewModel<TodosViewState, TodosEvent>(initialState = TodosViewState()) {

    private var todos: List<TodoItem> = listOf()
    private var showedTodos: List<TodoItem> = listOf()
    private var completeCnt = 0
    private var completedShowed = false
    private var networkState: NetworkConnectionState = NetworkConnectionState.Available

    init {
        viewModelScope.launch {
            repository.getAllTodosFromBD()
            repository.refreshAllTodos()
            repository
                .getTodos()
                .collect {
                    todos = it
                    updateTodos()
                }
        }
        viewModelScope.launch {
            repository
                .getErrors()
                .onEach { errorId ->
                    viewState.update { it.copy(errorId = errorId) }
                }
                .collect()
        }
    }

    override fun obtainEvent(viewEvent: TodosEvent) {
        when (viewEvent) {
            is TodosEvent.CompleteTodo -> onCompleteTodo(viewEvent.id)
            TodosEvent.ClickShowCompletedTodos -> clickShowCompletedTodos()
            is TodosEvent.DeleteTodo -> deleteTodo(viewEvent.id)
            TodosEvent.CancelJobs -> viewModelScope.cancel()
            is TodosEvent.HandleSnackbarResult -> handleSnackbarResult(viewEvent.result)
            TodosEvent.RefreshTodos -> refreshTodos()
            is TodosEvent.HandleNetworkState -> handleNetworkState(viewEvent.networkConnectionState)
        }
    }

    private fun handleNetworkState(connectionState: NetworkConnectionState) {
        networkState = connectionState
        when (connectionState) {
            NetworkConnectionState.Available -> {
                viewState.update { it.copy(errorId = null, connectionState = connectionState) }
                repository.sendAllTodos(todos)
            }

            NetworkConnectionState.Unavailable -> {
                viewState.update {
                    it.copy(
                        errorId = R.string.network_unavailable, connectionState = connectionState
                    )
                }
            }
        }
    }

    private fun refreshTodos() {
        repository.refreshAllTodos {
            viewState.update { it.copy(refreshing = false) }
        }
        viewState.update { it.copy(refreshing = true) }
    }

    private fun handleSnackbarResult(result: SnackbarResult) {
        when (result) {
            SnackbarResult.Dismissed -> {}
            SnackbarResult.ActionPerformed -> {
                repository.sendAllTodos(todos)
                viewState.update { it.copy(errorId = null) }
            }
        }
    }

    private fun updateTodos() {
        showedTodos = getShowedTodos()
        completeCnt = todos.count { it.done }

        viewState.update {
            it.copy(
                todos = showedTodos, completeCnt = completeCnt, errorId = null, refreshing = false
            )
        }
    }

    private fun deleteTodo(id: String) {
        repository.deleteTodo(id)
    }

    private fun onCompleteTodo(id: String) {
        val foundTodo = repository.getTodoById(id) ?: return
        val newTodo = foundTodo.copy(done = !foundTodo.done)

        repository.updateTodo(newTodo)
    }

    private fun clickShowCompletedTodos() {
        completedShowed = !completedShowed
        showedTodos = getShowedTodos()
        viewState.update { it.copy(todos = showedTodos, completedShowed = completedShowed) }
    }

    private fun getShowedTodos(): List<TodoItem> {
        return if (completedShowed) todos.toList() else todos.filter { !it.done }
    }
}