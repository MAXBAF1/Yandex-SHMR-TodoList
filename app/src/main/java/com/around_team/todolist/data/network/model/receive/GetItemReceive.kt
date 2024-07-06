package com.around_team.todolist.data.network.model.receive

import com.around_team.todolist.data.network.model.TodoItemDTO
import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response received when requesting a single item.
 *
 * @property status The status of the response, default is "ok".
 * @property todo The todo item received in the response.
 */
data class GetItemReceive(
    @SerializedName("status") val status: String = "ok",
    @SerializedName("element") val todo: TodoItemDTO,
) : ReceiveDTO()