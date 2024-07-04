package com.around_team.todolist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.around_team.todolist.data.db.entities.TodoItemEntity

@Database(
    entities = [TodoItemEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class TodoListDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}