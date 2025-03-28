package com.around_team.todolist.ui.common.models

import androidx.compose.ui.graphics.Color
import com.around_team.todolist.data.db.entities.TodoItemEntity
import com.around_team.todolist.data.network.model.TodoItemDTO
import com.around_team.todolist.ui.common.enums.TodoImportance
import com.around_team.todolist.utils.toHex
import java.util.Date

/**
 * Data class representing a Todo item.
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
    val color: Color? = null,
    val files: List<String>? = null,
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
            lastUpdatedBy,
            color?.toHex(),
            files
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
            lastUpdatedBy,
            color?.toHex(),
            files,
        )
    }
}