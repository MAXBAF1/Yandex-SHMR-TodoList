package com.around_team.todolist.ui.screens.todos.models

import com.around_team.todolist.ui.common.models.TodoItem

sealed class TodosEvent {
    data class AddNewTodo(val todo: TodoItem, val delete: Boolean) : TodosEvent()
    data object ClickShowCompletedTodos : TodosEvent()
    data class CompleteTodo(val id: String): TodosEvent()
}