package com.around_team.todolist.data.network.model.receive

import com.google.gson.annotations.SerializedName

/**
 * Base class for data transfer objects received from the server.
 *
 * @property revision The revision number of the data, default is 0.
 */
open class ReceiveDTO(@SerializedName("revision") val revision: Int = 0)