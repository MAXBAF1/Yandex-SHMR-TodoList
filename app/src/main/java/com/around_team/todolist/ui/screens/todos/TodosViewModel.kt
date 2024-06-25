package com.around_team.todolist.ui.screens.todos

import androidx.lifecycle.viewModelScope
import com.around_team.todolist.data.db.TodoItemsRepository
import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.data.model.TodoItem
import com.around_team.todolist.ui.screens.todos.models.TodosEvent
import com.around_team.todolist.ui.screens.todos.models.TodosViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val repository: TodoItemsRepository,
) : BaseViewModel<TodosViewState, TodosEvent>(initialState = TodosViewState()) {

    private var todos: MutableList<TodoItem> = mutableListOf()
    private var showedTodos: List<TodoItem> = listOf()
    private var completeCnt = 0
    private var completedShowed = false

    override fun obtainEvent(viewEvent: TodosEvent) {
        when (viewEvent) {
            is TodosEvent.CompleteTodo -> clickCompleteTodo(viewEvent.id)
            TodosEvent.ClickShowCompletedTodos -> clickShowCompletedTodos()
            is TodosEvent.DeleteTodo -> addOrDeleteTodo(viewEvent.id)
            TodosEvent.UpdateTodos -> viewModelScope.launch { updateTodos() }
        }
    }

    private suspend fun updateTodos() {
        todos = repository.getAllTodos().toMutableList()
        showedTodos = getShowedTodos()
        completeCnt = todos.count { it.completed }

        viewState.update { it.copy(todos = showedTodos, completeCnt = completeCnt) }
    }

    private fun addOrDeleteTodo(id: String) {
        viewModelScope.launch {
            repository.deleteTodo(id)
            updateTodos()
        }
    }

    private fun clickShowCompletedTodos() {
        completedShowed = !completedShowed
        showedTodos = getShowedTodos()
        viewState.update { it.copy(todos = showedTodos, completedShowed = completedShowed) }
    }

    private fun clickCompleteTodo(id: String) {
        viewModelScope.launch {
            repository.completeTodo(id)
            updateTodos()
        }
    }

    private fun getShowedTodos(): List<TodoItem> {
        return if (completedShowed) todos.toList() else todos.filter { !it.completed }
    }
}