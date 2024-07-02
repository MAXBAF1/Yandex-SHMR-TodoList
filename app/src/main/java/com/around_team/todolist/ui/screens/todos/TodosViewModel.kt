package com.around_team.todolist.ui.screens.todos

import androidx.lifecycle.viewModelScope
import com.around_team.todolist.data.db.TodoItemsRepository
import com.around_team.todolist.data.model.TodoItem
import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.screens.todos.models.TodosEvent
import com.around_team.todolist.ui.screens.todos.models.TodosViewState
import com.around_team.todolist.utils.ExceptionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val repository: TodoItemsRepository,
    private val exceptionHandler: ExceptionHandler,
) : BaseViewModel<TodosViewState, TodosEvent>(initialState = TodosViewState()) {

    private var todos: List<TodoItem> = listOf()
    private var showedTodos: List<TodoItem> = listOf()
    private var completeCnt = 0
    private var completedShowed = false

    init {
        viewModelScope.launch {
            exceptionHandler.handleException {
                repository.getAllTodos()
                repository.getTodos().collect {
                    todos = it
                    updateTodos()
                }
            }
        }
    }

    override fun obtainEvent(viewEvent: TodosEvent) {
        when (viewEvent) {
            is TodosEvent.CompleteTodo -> clickCompleteTodo(viewEvent.id)
            TodosEvent.ClickShowCompletedTodos -> clickShowCompletedTodos()
            is TodosEvent.DeleteTodo -> deleteTodo(viewEvent.id)
            TodosEvent.CancelJobs -> viewModelScope.cancel()
        }
    }

    private fun updateTodos() {
        showedTodos = getShowedTodos()
        completeCnt = todos.count { it.done }

        viewState.update { it.copy(todos = showedTodos, completeCnt = completeCnt) }
    }

    private fun deleteTodo(id: String) {
        viewModelScope.launch {
            exceptionHandler.handleException {
                repository.deleteTodo(id)
            }
        }
    }

    private fun clickShowCompletedTodos() {
        completedShowed = !completedShowed
        showedTodos = getShowedTodos()
        viewState.update { it.copy(todos = showedTodos, completedShowed = completedShowed) }
    }

    private fun clickCompleteTodo(id: String) {
        viewModelScope.launch {
            exceptionHandler.handleException {
                repository.clickCompleteTodo(id)
            }
        }
    }

    private fun getShowedTodos(): List<TodoItem> {
        return if (completedShowed) todos.toList() else todos.filter { !it.done }
    }
}