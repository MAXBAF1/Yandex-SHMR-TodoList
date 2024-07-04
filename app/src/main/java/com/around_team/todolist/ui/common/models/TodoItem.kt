package com.around_team.todolist.ui.common.models

import com.around_team.todolist.data.db.entities.TodoItemEntity
import com.around_team.todolist.data.network.model.TodoItemDTO
import com.around_team.todolist.ui.common.enums.TodoImportance
import java.util.Date

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
