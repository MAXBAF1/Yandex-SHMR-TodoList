package com.around_team.todolist.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.around_team.todolist.data.db.entities.TodoItemEntity

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodos(todos: List<TodoItemEntity>)

    @Query("SELECT * FROM todo_list")
    suspend fun getAllTodos(): List<TodoItemEntity>

    @Query("DELETE FROM todo_list WHERE id = :todoId")
    suspend fun deleteTodoById(todoId: String)

    @Query("DELETE FROM todo_list")
    suspend fun deleteAllTodos()
}