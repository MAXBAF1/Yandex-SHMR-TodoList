package com.around_team.todolist.ui.screens.todos.models

sealed class TodosEvent {
    data class ShowCompletedTodos(val show: Boolean = true): TodosEvent()
    data class CompleteTodo(val id: String): TodosEvent()
}