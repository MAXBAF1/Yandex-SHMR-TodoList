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
import com.around_team.todolist.utils.PreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and business logic related to todos.
 *
 * @param repository The repository interface providing access to data operations.
 * @param preferencesHelper Helper class for managing application preferences.
 */
@HiltViewModel
class TodosViewModel @Inject constructor(
    private val repository: Repository,
    private val preferencesHelper: PreferencesHelper,
) : BaseViewModel<TodosViewState, TodosEvent>(initialState = TodosViewState()) {

    private var collectingStarted = false
    private var todos: List<TodoItem> = listOf()
    private var showedTodos: List<TodoItem> = listOf()
    private var completeCnt = 0
    private var completedShowed = false
    private var networkState: NetworkConnectionState = NetworkConnectionState.Available

    override fun obtainEvent(viewEvent: TodosEvent) {
        when (viewEvent) {
            is TodosEvent.CompleteTodo -> onCompleteTodo(viewEvent.id)
            TodosEvent.ClickShowCompletedTodos -> clickShowCompletedTodos()
            is TodosEvent.DeleteTodo -> deleteTodo(viewEvent.id)
            TodosEvent.CancelJobs -> viewModelScope.cancel()
            is TodosEvent.HandleSnackbarResult -> handleSnackbarResult(viewEvent.result)
            TodosEvent.RefreshTodos -> refreshTodos()
            is TodosEvent.HandleNetworkState -> handleNetworkState(viewEvent.networkConnectionState)
            TodosEvent.StartCollecting -> startCollecting()
        }
    }

    private fun startCollecting() {
        if (!collectingStarted) {
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
                        viewState.update { it.copy(messageId = errorId, refreshing = false) }
                    }
                    .collect()
            }
        }
        collectingStarted = true
    }

    private fun handleNetworkState(connectionState: NetworkConnectionState) {
        networkState = connectionState
        when (connectionState) {
            NetworkConnectionState.Available -> {
                viewState.update { it.copy(messageId = null, connectionState = connectionState) }
                repository.sendAllTodos(todos) {
                    viewState.update { it.copy(messageId = R.string.success_sync) }
                }
            }

            NetworkConnectionState.Unavailable -> {
                viewState.update {
                    it.copy(
                        messageId = R.string.network_unavailable, connectionState = connectionState
                    )
                }
            }
        }
    }

    private fun refreshTodos() {
        repository.sendAllTodos(todos) {
            viewState.update { it.copy(refreshing = false, messageId = R.string.success_sync) }
        }
        viewState.update { it.copy(refreshing = true, messageId = null) }
    }

    private fun handleSnackbarResult(result: SnackbarResult) {
        when (result) {
            SnackbarResult.Dismissed -> {}
            SnackbarResult.ActionPerformed -> {
                repository.sendAllTodos(todos) {
                    viewState.update { it.copy(messageId = R.string.success_sync) }
                }
                viewState.update { it.copy(messageId = null) }
            }
        }
    }

    private fun updateTodos() {
        showedTodos = getShowedTodos()
        completeCnt = todos.count { it.done }

        viewState.update {
            it.copy(
                todos = showedTodos, completeCnt = completeCnt, messageId = null, refreshing = false
            )
        }
    }

    private fun deleteTodo(id: String) {
        repository.deleteTodo(id)
    }

    private fun onCompleteTodo(id: String) {
        val foundTodo = repository.getTodoById(id) ?: return
        val newTodo = foundTodo.copy(
            done = !foundTodo.done,
            modifiedDate = Date().time,
            lastUpdatedBy = preferencesHelper.getUUID()
        )

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