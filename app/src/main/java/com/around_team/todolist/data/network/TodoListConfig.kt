package com.around_team.todolist.data.network

enum class TodoListConfig(private val value: String) {
    HOST_ADDRESS("https://hive.mrdekk.ru/todo"),
    LIST_ADDRESS("$HOST_ADDRESS/list");

    override fun toString(): String = value

    companion object {
        fun getElementListAddress(id: String): String = "$LIST_ADDRESS/$id"
    }
}