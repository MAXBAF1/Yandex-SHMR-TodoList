package com.around_team.todolist.data.db

import com.around_team.todolist.ui.common.enums.TodoPriority
import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.utils.find
import java.util.Date
import javax.inject.Inject

class TodoItemsRepository @Inject constructor() {
    private val todos = mutableListOf(
        TodoItem("1", "Купить что-то", TodoPriority.Medium, false, ""),
        TodoItem(
            "2",
            "Купить что-то, где-то, зачем-то, но зачем не очень понятно",
            TodoPriority.Medium,
            false,
            ""
        ),
        TodoItem(
            "3",
            "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрезан текст",
            TodoPriority.Medium,
            false,
            ""
        ),
        TodoItem("4", "Купить что-то", TodoPriority.Low, false, ""),
        TodoItem("5", "Купить что-то", TodoPriority.High, false, ""),
        TodoItem("6", "Купить что-то", TodoPriority.Medium, true, ""),
        TodoItem("7", "Купить что-то", TodoPriority.Medium, false, "", deadline = Date().time),
        TodoItem("8", "Купить что-то", TodoPriority.Medium, false, ""),
        TodoItem("9", "Купить что-то", TodoPriority.Medium, false, ""),
        TodoItem("10", "Купить что-то", TodoPriority.Medium, false, ""),
        TodoItem("11", "Купить что-то", TodoPriority.Medium, false, ""),
        TodoItem("12", "Купить что-то", TodoPriority.Medium, false, ""),
        TodoItem("13", "Купить что-то", TodoPriority.Medium, false, ""),
        TodoItem("14", "Купить что-то", TodoPriority.Medium, false, ""),
        TodoItem("15", "Купить что-то", TodoPriority.Medium, false, ""),
    )

    suspend fun getAllTodos(): List<TodoItem> {
        return todos
    }

    suspend fun addOrEditTodo(todoItem: TodoItem): Boolean {
        val index = todos.find(todoItem.id).first
        if (index == -1) todos.add(todoItem) else todos[index] = todoItem

        return true
    }

    suspend fun deleteTodo(todoItem: TodoItem): Boolean {
        val index = todos.find(todoItem.id).first
        todos.removeAt(index)

        return true
    }
}
