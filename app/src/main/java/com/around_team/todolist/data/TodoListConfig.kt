package com.around_team.todolist.data

enum class TodoListConfig(private val value: String) {
    HOST_ADDRESS("https://hive.mrdekk.ru/todo"),
    LIST_ADDRESS("$HOST_ADDRESS/list");

    fun getElementListAddress(id: String): String = "$LIST_ADDRESS/$id"

    override fun toString(): String = value
}