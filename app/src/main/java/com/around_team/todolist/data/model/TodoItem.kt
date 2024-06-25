package com.around_team.todolist.data.model

import com.around_team.todolist.ui.common.enums.TodoPriority

data class TodoItem(
    val id: String,
    val text: String,
    val priority: TodoPriority,
    val completed: Boolean,
    val creationDate: String,
    val modifiedDate: String? = null,
    val deadline: Long? = null,
)
