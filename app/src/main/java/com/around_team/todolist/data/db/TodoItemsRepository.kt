package com.around_team.todolist.data.db

import com.around_team.todolist.data.MyCustomBackend
import com.around_team.todolist.data.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoItemsRepository @Inject constructor() {
    private val todos = MutableStateFlow(listOf<TodoItem>())

    fun getTodos(): StateFlow<List<TodoItem>> = todos

    suspend fun getAllTodos() {
        withContext(Dispatchers.IO) {
            todos.update { MyCustomBackend.getAllTodos() }
        }
    }

    suspend fun getTodoById(id: String): TodoItem? {
        return todos.value.find { it.id == id }
    }

    suspend fun saveTodo(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            MyCustomBackend.addTodo(todoItem)
            todos.update { MyCustomBackend.getAllTodos() }

        }
    }

    suspend fun deleteTodo(id: String) {
        withContext(Dispatchers.IO) {
            MyCustomBackend.deleteTodo(id)
            todos.update { MyCustomBackend.getAllTodos() }
        }
    }

    suspend fun clickCompleteTodo(id: String) {
        withContext(Dispatchers.IO) {
            val foundTodo: TodoItem? = todos.value.find { it.id == id }
            if (foundTodo != null) {
                val newTodo = foundTodo.copy(done = !foundTodo.done)
                MyCustomBackend.updateTodo(id, newTodo)
                todos.update { MyCustomBackend.getAllTodos() }
            }
        }
    }
}
