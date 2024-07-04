package com.around_team.todolist.data.db

import androidx.room.Transaction
import com.around_team.todolist.data.db.entities.TodoItemEntity
import com.around_team.todolist.ui.common.models.TodoItem
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val dao: Dao,
) {
    suspend fun insertTodo(todo: TodoItem) = dao.insertTodo(todo.toEntity())
    suspend fun getAllTodos(): List<TodoItem> = dao.getAllTodos().map { it.toTodoItem() }
    suspend fun deleteTodoById(todoId: String) = dao.deleteTodoById(todoId)
    @Transaction
    suspend fun replaceTodos(todos: List<TodoItem>) {
        dao.deleteAllTodos()
        dao.insertTodos(todos.map { it.toEntity() })
    }
}