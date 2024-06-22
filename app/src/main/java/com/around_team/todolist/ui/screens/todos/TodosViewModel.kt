package com.around_team.todolist.ui.screens.todos

import androidx.lifecycle.viewModelScope
import com.around_team.todolist.data.db.TodoItemsRepository
import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.ui.screens.todos.models.TodosEvent
import com.around_team.todolist.ui.screens.todos.models.TodosViewState
import com.around_team.todolist.utils.find
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

    init {
        viewModelScope.launch {
            todos = repository.getAllTodos().toMutableList()

            showedTodos = todos.filter { !it.completed }
            completeCnt = todos.size - showedTodos.size

            viewState.update { it.copy(todos = showedTodos, completeCnt = completeCnt) }
        }
    }

    override fun obtainEvent(viewEvent: TodosEvent) {
        when (viewEvent) {
            is TodosEvent.CompleteTodo -> completeTodo(viewEvent.id)
            TodosEvent.ClickShowCompletedTodos -> clickShowCompletedTodos()
            is TodosEvent.AddNewTodo -> addOrDeleteTodo(viewEvent.todo, viewEvent.delete)
        }
    }

    private fun addOrDeleteTodo(newTodo: TodoItem, delete: Boolean) {
        viewModelScope.launch {
            if (delete) {
                repository.deleteTodo(newTodo)
            } else repository.addOrEditTodo(newTodo)

            todos = repository.getAllTodos().toMutableList()
            showedTodos = getShowedTodos()
            completeCnt = todos.count { it.completed }
            viewState.update { it.copy(todos = showedTodos, completeCnt = completeCnt) }
        }
    }

    private fun clickShowCompletedTodos() {
        completedShowed = !completedShowed
        showedTodos = getShowedTodos()
        viewState.update { it.copy(todos = showedTodos, completedShowed = completedShowed) }
    }

    private fun completeTodo(id: String) {
        val (index: Int, foundTodo: TodoItem?) = todos.find(id)

        if (foundTodo != null) {
            todos[index] = foundTodo.copy(completed = !foundTodo.completed)
        }

        showedTodos = getShowedTodos()
        completeCnt = todos.count { it.completed }
        viewState.update { it.copy(todos = showedTodos, completeCnt = completeCnt) }
    }

    private fun getShowedTodos(): List<TodoItem> {
        return if (completedShowed) todos.toList() else todos.filter { !it.completed }
    }
}