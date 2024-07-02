package com.around_team.todolist.data.repositories

import com.around_team.todolist.data.MyCustomBackend
import com.around_team.todolist.data.model.TodoItem
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoItemsRepository @Inject constructor() {
    private val todos = MutableStateFlow(listOf<TodoItem>())
    private val errors = MutableSharedFlow<Throwable>(replay = 2)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        errors.tryEmit(throwable)
    }

    private val job = SupervisorJob()
    private val repositoryScope = CoroutineScope(Dispatchers.IO + job + exceptionHandler)

    fun getTodos(): StateFlow<List<TodoItem>> = todos
    fun getErrors(): Flow<Throwable> = errors

    fun getAllTodos() {
        repositoryScope.launch {
            todos.update { MyCustomBackend.getAllTodos() }
        }
    }

    fun getTodoById(id: String): TodoItem? {
        return todos.value.find { it.id == id }
    }

    fun saveTodo(todoItem: TodoItem, onSuccess: () -> Unit = {}) {
        repositoryScope.launch {
            MyCustomBackend.addTodo(todoItem)
            todos.update { MyCustomBackend.getAllTodos() }
            onSuccess()
        }
    }

    fun deleteTodo(id: String, onSuccess: () -> Unit = {}) {
        repositoryScope.launch {
            MyCustomBackend.deleteTodo(id)
            todos.update { MyCustomBackend.getAllTodos() }
            onSuccess()
        }
    }

    fun clickCompleteTodo(id: String) {
        repositoryScope.launch {
            val foundTodo: TodoItem? = todos.value.find { it.id == id }
            if (foundTodo != null) {
                val newTodo = foundTodo.copy(done = !foundTodo.done)
                MyCustomBackend.updateTodo(id, newTodo)
                todos.update { MyCustomBackend.getAllTodos() }
            }
        }
    }
}
