package com.around_team.todolist.data.network

/**
 * Configuration enum for Todo list related endpoints and addresses.
 *
 * @property value The string value representing the endpoint or address.
 */
enum class NetworkConfig(private val value: String) {
    HOST_ADDRESS("https://hive.mrdekk.ru/todo"),
    LIST_ADDRESS("$HOST_ADDRESS/list");

    override fun toString(): String = value

    companion object {
        fun getElementListAddress(id: String): String = "$LIST_ADDRESS/$id"
    }
}