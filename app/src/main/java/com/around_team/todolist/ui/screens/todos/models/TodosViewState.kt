package com.around_team.todolist.ui.screens.todos.models

import com.around_team.todolist.ui.common.enums.TodoPriority
import com.around_team.todolist.ui.common.models.TodoItem

data class TodosViewState(
    val todos: List<TodoItem> = listOf(
        TodoItem("1", "Купить что-то", TodoPriority.Medium, false, ""),
        TodoItem("1", "Купить что-то, где-то, зачем-то, но зачем не очень понятно", TodoPriority.Medium, false, ""),
        TodoItem("1", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрезан текст", TodoPriority.Medium, false, ""),
        TodoItem("1", "Купить что-то", TodoPriority.Low, false, ""),
        TodoItem("1", "Купить что-то", TodoPriority.High, false, ""),
        TodoItem("1", "Купить что-то", TodoPriority.Medium, true, ""),
        TodoItem("1", "Купить что-то", TodoPriority.Medium, false, "", deadline = "14 июня"),
    ),
    val completeCnt: Int = 0,
)