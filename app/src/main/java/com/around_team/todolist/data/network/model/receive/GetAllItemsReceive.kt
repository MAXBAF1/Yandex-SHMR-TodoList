package com.around_team.todolist.data.network.model.receive

import com.around_team.todolist.data.network.model.TodoItemDTO
import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response received when requesting all items.
 *
 * @property status The status of the response, default is "ok".
 * @property list The list of Todo items received in the response.
 */
data class GetAllItemsReceive(
    @SerializedName("status") val status: String = "ok",
    @SerializedName("list") val list: List<TodoItemDTO>,
) : ReceiveDTO()