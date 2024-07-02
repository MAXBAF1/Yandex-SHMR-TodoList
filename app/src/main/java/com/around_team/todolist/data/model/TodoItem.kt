package com.around_team.todolist.data.model

import com.around_team.todolist.ui.common.enums.TodoImportance
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoItem(
    @SerialName("id") val id: String,
    @SerialName("text") val text: String,
    @SerialName("importance") val importance: TodoImportance,
    @SerialName("deadline") val deadline: Long? = null,
    @SerialName("done") val done: Boolean,
    @SerialName("color") val color: String = "#FFFFFF",
    @SerialName("created_at") val creationDate: Long,
    @SerialName("changed_at") val modifiedDate: Long? = null,
    @SerialName("last_updated_by") val lastUpdatedBy: String? = "0",
)
