package com.around_team.todolist.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.around_team.todolist.ui.common.enums.TodoImportance
import com.around_team.todolist.ui.common.models.TodoItem
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.Locale

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