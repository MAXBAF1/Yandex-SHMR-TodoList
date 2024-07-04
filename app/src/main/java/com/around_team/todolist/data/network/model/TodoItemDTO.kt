package com.around_team.todolist.data.network.model

import com.around_team.todolist.ui.common.enums.TodoImportance
import com.around_team.todolist.ui.common.models.TodoItem
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.Locale

data class TodoItemDTO(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("importance") val importance: String,
    @SerializedName("deadline") val deadline: Long? = null,
    @SerializedName("done") val done: Boolean,
    @SerializedName("created_at") val creationDate: Long,
    @SerializedName("changed_at") val modifiedDate: Long = Date().time,
    @SerializedName("last_updated_by") val lastUpdatedBy: String = "1",
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