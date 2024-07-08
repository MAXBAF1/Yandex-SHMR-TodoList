package com.around_team.todolist.ui.common.models

import com.around_team.todolist.data.db.entities.TodoItemEntity
import com.around_team.todolist.data.network.model.TodoItemDTO
import com.around_team.todolist.ui.common.enums.TodoImportance
import java.util.Date

/**
 * Data class representing a Todo item.
 *
 * @property id The unique identifier of the Todo item.
 * @property text The text describing the Todo item.
 * @property importance The importance level of the Todo item.
 * @property deadline The deadline timestamp for the Todo item (nullable).
 * @property done Indicates whether the Todo item is completed or not.
 * @property creationDate The timestamp when the Todo item was created.
 * @property modifiedDate The timestamp when the Todo item was last modified (default to current time).
 * @property lastUpdatedBy The identifier of the user who last updated the Todo item (default to "1").
 */
data class TodoItem(
    val id: String,
    val text: String,
    val importance: TodoImportance,
    val deadline: Long? = null,
    val done: Boolean,
    val creationDate: Long,
    val modifiedDate: Long = Date().time,
    val lastUpdatedBy: String = "1",
) {
    /**
     * Converts this TodoItem to its corresponding data transfer object (DTO).
     *
     * @return The TodoItemDTO representing this TodoItem.
     */
    fun toDTO(): TodoItemDTO {
        return TodoItemDTO(
            id,
            text,
            importance.name.lowercase(),
            deadline,
            done,
            creationDate,
            modifiedDate,
            lastUpdatedBy
        )
    }

    /**
     * Converts this TodoItem to its corresponding database entity (Entity).
     *
     * @return The TodoItemEntity representing this TodoItem.
     */
    fun toEntity(): TodoItemEntity {
        return TodoItemEntity(
            id,
            text,
            importance.name.lowercase(),
            deadline,
            done,
            creationDate,
            modifiedDate,
            lastUpdatedBy
        )
    }
}