package com.around_team.todolist.ui.screens.todos.models

sealed class TodosEvent {
    data object ClickShowCompletedTodos : TodosEvent()
    data class CompleteTodo(val id: String): TodosEvent()
}