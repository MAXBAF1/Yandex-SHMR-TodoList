package com.around_team.todolist.ui.screens.todos

import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.viewModelScope
import com.around_team.todolist.data.model.TodoItem
import com.around_team.todolist.data.repositories.TodoItemsRepository
import com.around_team.todolist.ui.common.models.BaseViewModel
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
    private val repository: TodoItemsRepository,
) : BaseViewModel<TodosViewState, TodosEvent>(initialState = TodosViewState()) {

    private var todos: List<TodoItem> = listOf()
    private var showedTodos: List<TodoItem> = listOf()
    private var completeCnt = 0
    private var completedShowed = false
    private var lastOperation: () -> Unit = { repository.refreshAllTodos() }

    init {
        viewModelScope.launch {
            repository.refreshAllTodos()
            repository.getTodos().collect {
                todos = it
                updateTodos()
            }
        }
        viewModelScope.launch {
            repository.getErrors().onEach { error ->
                viewState.update { it.copy(error = error.message) }
            }.collect()
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
                lastOperation()
                viewState.update { it.copy(error = null) }
            }
        }
    }

    private fun updateTodos() {
        showedTodos = getShowedTodos()
        completeCnt = todos.count { it.done }

        viewState.update {
            it.copy(
                todos = showedTodos, completeCnt = completeCnt, error = null, refreshing = false
            )
        }
    }

    private fun deleteTodo(id: String) {
        repository.deleteTodo(id)
        lastOperation = { repository.deleteTodo(id) }
    }

    private fun clickShowCompletedTodos() {
        completedShowed = !completedShowed
        showedTodos = getShowedTodos()
        viewState.update { it.copy(todos = showedTodos, completedShowed = completedShowed) }
    }

    private fun onCompleteTodo(id: String) {
        repository.clickCompleteTodo(id)
        lastOperation = { repository.clickCompleteTodo(id) }
    }

    private fun getShowedTodos(): List<TodoItem> {
        return if (completedShowed) todos.toList() else todos.filter { !it.done }
    }
}