package com.around_team.todolist.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.around_team.todolist.ui.common.enums.TodoImportance
import com.around_team.todolist.ui.common.models.TodoItem
import java.util.Locale

/**
 * Data class representing a Todo item entity in the local database.
 *
 * @property id The unique identifier for the todo item.
 * @property text The description text of the todo item.
 * @property importance The importance level of the todo item.
 * @property deadline The deadline for the todo item, represented as a timestamp. Nullable.
 * @property done A boolean indicating whether the todo item is done.
 * @property creationDate The creation date of the todo item, represented as a timestamp.
 * @property modifiedDate The last modified date of the todo item, represented as a timestamp.
 * @property lastUpdatedBy The identifier of the user who last updated the todo item, default is "1".
 */
@Entity(tableName = "todo_list")
data class TodoItemEntity(
    @PrimaryKey val id: String,
    @ColumnInfo("text") val text: String,
    @ColumnInfo("importance") val importance: String,
    @ColumnInfo("deadline") val deadline: Long? = null,
    @ColumnInfo("done") val done: Boolean,
    @ColumnInfo("created_at") val creationDate: Long,
    @ColumnInfo("changed_at") val modifiedDate: Long,
    @ColumnInfo("last_updated_by") val lastUpdatedBy: String = "1",
) {
    fun toTodoItem(): TodoItem {
        return TodoItem(
            id,
            text,
            TodoImportance.valueOf(importance.replaceFirstChar { it.titlecase(Locale.ENGLISH) }),
            deadline,
            done,
            creationDate,
            modifiedDate,
            lastUpdatedBy,
        )
    }
}