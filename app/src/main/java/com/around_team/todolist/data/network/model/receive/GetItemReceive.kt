package com.around_team.todolist.data.network.model.receive

import com.around_team.todolist.data.network.model.TodoItemDTO
import com.google.gson.annotations.SerializedName


data class GetItemReceive(
    @SerializedName("status") val status: String = "ok",
    @SerializedName("element") val todo: TodoItemDTO,
) : ReceiveDTO()