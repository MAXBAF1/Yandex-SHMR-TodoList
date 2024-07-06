package com.around_team.todolist.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.around_team.todolist.data.db.entities.TodoItemEntity

/**
 * Data Access Object (DAO) for accessing the Todo items in the local database.
 */
@Dao
interface Dao {

    /**
     * Inserts a todo item into the database. If a conflict occurs, the existing record will be replaced.
     *
     * @param todo The todo item entity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoItemEntity)

    /**
     * Inserts a list of todo items into the database. If a conflict occurs, the existing records will be replaced.
     *
     * @param todos The list of todo item entities to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodos(todos: List<TodoItemEntity>)

    /**
     * Retrieves all todo items from the database.
     *
     * @return A list of all todo item entities.
     */
    @Query("SELECT * FROM todo_list")
    suspend fun getAllTodos(): List<TodoItemEntity>

    /**
     * Deletes a todo item from the database by its ID.
     *
     * @param todoId The unique identifier of the todo item to delete.
     */
    @Query("DELETE FROM todo_list WHERE id = :todoId")
    suspend fun deleteTodoById(todoId: String)

    /**
     * Deletes all todo items from the database.
     */
    @Query("DELETE FROM todo_list")
    suspend fun deleteAllTodos()
}
