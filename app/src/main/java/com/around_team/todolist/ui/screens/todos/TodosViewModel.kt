package com.around_team.todolist.ui.screens.todos

import androidx.lifecycle.viewModelScope
import com.around_team.todolist.data.db.TodoItemsRepository
import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.common.models.TodoItem
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
    private var completeCtn = 0
    private var completedShowed = false

    init {
        viewModelScope.launch {
            todos = repository.getAllTodos().toMutableList()

            showedTodos = todos.filter { !it.completed }
            completeCtn = todos.size - showedTodos.size

            viewState.update { it.copy(todos = showedTodos, completeCnt = completeCtn) }
        }
    }

    override fun obtainEvent(viewEvent: TodosEvent) {
        when (viewEvent) {
            is TodosEvent.CompleteTodo -> completeTodo(viewEvent.id)
            TodosEvent.ClickShowCompletedTodos -> clickShowCompletedTodos()
        }
    }

    private fun clickShowCompletedTodos() {
        completedShowed = !completedShowed
        showedTodos = if (completedShowed) todos.toList() else todos.filter { !it.completed }
        viewState.update { it.copy(todos = showedTodos, completedShowed = completedShowed) }
    }

    private fun completeTodo(id: String) {
        var index = -1
        var foundTodo: TodoItem? = null
        todos.forEachIndexed { i, todo ->
            if (todo.id == id) {
                index = i
                foundTodo = todo
                return@forEachIndexed
            }
        }

        if (foundTodo != null) {
            todos[index] = foundTodo!!.copy(completed = !foundTodo!!.completed)
        }

        showedTodos = if (completedShowed) todos.toList() else todos.filter { !it.completed }
        completeCtn = todos.count { it.completed }
        viewState.update { it.copy(todos = showedTodos, completeCnt = completeCtn) }
    }
}