package com.around_team.todolist.data.network.model.request

import com.around_team.todolist.data.network.model.TodoItemDTO
import com.google.gson.annotations.SerializedName

data class SendAllItemsRequest(
    @SerializedName("list") val todos: List<TodoItemDTO>,
) : RequestDTO