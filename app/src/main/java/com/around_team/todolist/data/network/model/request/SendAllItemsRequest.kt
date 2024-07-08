package com.around_team.todolist.data.network.model.request

import com.around_team.todolist.data.network.model.TodoItemDTO
import com.google.gson.annotations.SerializedName

/**
 * Data class representing the request to send a list of todo items.
 *
 * @property todos The list of todo items to be sent.
 */
data class SendAllItemsRequest(
    @SerializedName("list") val todos: List<TodoItemDTO>,
) : RequestDTO