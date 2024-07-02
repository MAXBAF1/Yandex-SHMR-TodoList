package com.around_team.todolist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllTodosDTO(
    @SerialName("status") val status: String,
    @SerialName("list") val todos: List<TodoItem>,
    @SerialName("revision") val revision: Int,
) : DTO()
