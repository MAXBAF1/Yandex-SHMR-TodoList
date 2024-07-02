package com.around_team.todolist.data

import android.net.http.NetworkException
import com.around_team.todolist.data.model.TodoItem
import com.around_team.todolist.ui.common.enums.TodoImportance
import com.around_team.todolist.utils.find
import java.util.Date
import kotlin.random.Random

object MyCustomBackend {
    private var todos = mutableListOf(
        TodoItem(
            "1", "Купить что-то", TodoImportance.Basic, creationDate = Date().time, done = false
        ),
        TodoItem(
            "2",
            "Купить что-то, где-то, зачем-то, но зачем не очень понятно",
            TodoImportance.Basic,
            creationDate = Date().time,
            done = false
        ),
        TodoItem(
            "3",
            "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрезан текст",
            TodoImportance.Basic,
            creationDate = Date().time,
            done = false
        ),
        TodoItem(
            "4", "Купить что-то", TodoImportance.Low, creationDate = Date().time, done = false
        ),
        TodoItem(
            "5", "Купить что-то", TodoImportance.Important, creationDate = Date().time, done = false
        ),
        TodoItem(
            "6", "Купить что-то", TodoImportance.Basic, creationDate = Date().time, done = true
        ),
        TodoItem(
            "7", "Купить что-то", TodoImportance.Basic, creationDate = Date().time, done = false
        ),
        TodoItem(
            "8", "Купить что-то", TodoImportance.Basic, creationDate = Date().time, done = false
        ),
        TodoItem(
            "9", "Купить что-то", TodoImportance.Basic, creationDate = Date().time, done = false
        ),
        TodoItem(
            "10", "Купить что-то", TodoImportance.Basic, creationDate = Date().time, done = false
        ),
        TodoItem(
            "11", "Купить что-то", TodoImportance.Basic, creationDate = Date().time, done = false
        ),
        TodoItem(
            "12", "Купить что-то", TodoImportance.Basic, creationDate = Date().time, done = false
        ),
        TodoItem(
            "13", "Купить что-то", TodoImportance.Basic, creationDate = Date().time, done = false
        ),
        TodoItem(
            "14", "Купить что-то", TodoImportance.Basic, creationDate = Date().time, done = false
        ),
        TodoItem(
            "15", "Купить что-то", TodoImportance.Basic, creationDate = Date().time, done = false
        ),
    )

    suspend fun getAllTodos(): List<TodoItem> {
        if (Random.nextInt(0, 2) == 0) throw RuntimeException("Random Error")
        return todos.toList()
    }

    suspend fun addTodo(todoItem: TodoItem) {
        if (Random.nextInt(0, 2) == 0) throw RuntimeException("Random Error")
        val index = todos.find(todoItem.id).first

        if (index == -1) todos.add(todoItem) else todos[index] = todoItem
    }

    suspend fun updateTodo(id: String, todoItem: TodoItem) {
        if (Random.nextInt(0, 2) == 0) throw RuntimeException("Random Error")
        val index: Int = todos.find(id).first

        if (index != -1) {
            todos[index] = todoItem
        }
    }

    suspend fun deleteTodo(id: String) {
        if (Random.nextInt(0, 2) == 0) throw RuntimeException("Random Error")

        val index = todos.find(id).first
        if (index != -1) {
            todos.removeAt(index)
        }
    }
}