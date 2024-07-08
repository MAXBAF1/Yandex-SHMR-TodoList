package com.around_team.todolist.data.network.model.request

import com.around_team.todolist.data.network.model.TodoItemDTO
import com.google.gson.annotations.SerializedName

/**
 * Data class representing the request to post a single todo item.
 *
 * @property todo The todo item to be posted.
 */
data class PostItemRequest(
    @SerializedName("element") val todo: TodoItemDTO,
) : RequestDTO