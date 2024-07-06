package com.around_team.todolist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.around_team.todolist.data.db.entities.TodoItemEntity

/**
 * Database class for the Todo List application.
 * This class defines the database configuration and serves as the main access point
 * for the underlying connection to your app's persisted relational data.
 *
 * @property entities The list of entities associated with the database.
 * @property version The version number of the database schema.
 * @property exportSchema Indicates whether to export the schema to a file.
 */
@Database(
    entities = [TodoItemEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class TodoListDatabase : RoomDatabase() {

    /**
     * Provides the Data Access Object (DAO) for interacting with the database.
     *
     * @return The DAO for the todo list database.
     */
    abstract fun dao(): Dao
}