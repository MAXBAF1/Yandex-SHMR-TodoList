package com.around_team.todolist.data.network.model.receive

import com.around_team.todolist.data.network.model.TodoItemDTO
import com.google.gson.annotations.SerializedName


data class GetAllItemsReceive(
    @SerializedName("status") val status: String = "ok",
    @SerializedName("list") val list: List<TodoItemDTO>,
) : ReceiveDTO()