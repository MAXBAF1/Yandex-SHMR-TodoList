package com.around_team.todolist.data.network.model

import com.around_team.todolist.ui.common.enums.TodoImportance
import com.around_team.todolist.ui.common.models.TodoItem
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.Locale

/**
 * Data class representing a Todo item for serialization/deserialization purposes.
 *
 * @property id The unique identifier of the todo item.
 * @property text The description text of the todo item.
 * @property importance The importance level of the todo item.
 * @property deadline The deadline for the todo item, represented as a timestamp. Nullable.
 * @property done A boolean indicating whether the todo item is done.
 * @property creationDate The creation date of the todo item, represented as a timestamp.
 * @property modifiedDate The last modified date of the todo item, default is the current time.
 * @property lastUpdatedBy The identifier of the user who last updated the todo item.
 */
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
    /**
     * Converts this DTO object to a domain model [TodoItem].
     *
     * @return The [TodoItem] domain model object.
     */
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