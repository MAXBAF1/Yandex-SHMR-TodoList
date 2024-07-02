package com.around_team.todolist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoDTO(
    @SerialName("element") val todo: TodoItem,
    @SerialName("status") val status: String? = null,
    @SerialName("revision") val revision: Int? = null,
) : DTO()
