package com.around_team.todolist.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import com.around_team.todolist.data.model.TodoItem

fun Modifier.background(color: () -> Color): Modifier {
    return drawBehind { drawRect(color()) }
}

fun List<TodoItem>.find(otherTodoId: String): Pair<Int, TodoItem?> {
    var index = -1
    var foundTodo: TodoItem? = null
    this.forEachIndexed { i, todo ->
        if (todo.id == otherTodoId) {
            index = i
            foundTodo = todo
            return@forEachIndexed
        }
    }

    return index to foundTodo
}