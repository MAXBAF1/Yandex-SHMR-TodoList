package com.around_team.todolist.ui.screens.edit.models

import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.ui.common.enums.TodoImportance
import java.util.Date
import java.util.UUID

/**
 * Data class representing the state related to editing or creating a todo item.
 * @property saveEnable Boolean indicating whether the "save" action is enabled.
 * @property editedTodo The todo item being edited or created.
 * @property deadlineChecked Boolean indicating whether the deadline is checked.
 * @property showCalendar Boolean indicating whether the calendar for selecting a deadline is shown.
 * @property toTodosScreen Boolean indicating whether to navigate back to the todos screen.
 * @property exception Boolean indicating the presence of an exception.
 */
data class EditViewState(
    val saveEnable: Boolean = false,
    val editedTodo: TodoItem = TodoItem(
        id = UUID.randomUUID().toString(),
        text = "",
        importance = TodoImportance.Basic,
        done = false,
        creationDate = Date().time
    ),
    val deadlineChecked: Boolean = false,
    val showCalendar: Boolean = true,
    val toTodosScreen: Boolean = false,
    val exception: Boolean = true,
)