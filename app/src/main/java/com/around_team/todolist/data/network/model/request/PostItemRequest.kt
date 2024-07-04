package com.around_team.todolist.data.network.model.request

import com.around_team.todolist.data.network.model.TodoItemDTO
import com.google.gson.annotations.SerializedName

data class PostItemRequest(
    @SerializedName("element") val todo: TodoItemDTO,
) : RequestDTO