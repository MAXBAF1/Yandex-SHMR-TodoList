package com.around_team.todolist.data.network.model.receive

import com.google.gson.annotations.SerializedName

open class ReceiveDTO(@SerializedName("revision") val revision: Int = 0)