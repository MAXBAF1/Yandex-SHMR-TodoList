package com.around_team.todolist.ui.screens.edit

import com.around_team.todolist.ui.common.enums.TodoPriority
import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.ui.screens.edit.models.EditEvent
import com.around_team.todolist.ui.screens.edit.models.EditViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor() :
    BaseViewModel<EditViewState, EditEvent>(initialState = EditViewState()) {
    private var saveEnable: Boolean = false
    private var editedTodo: TodoItem =
        TodoItem(UUID.randomUUID().toString(), "", TodoPriority.Medium, false, "")
    private var deadlineChecked: Boolean = false
    private var showCalendar: Boolean = true
    private var oldTodo: TodoItem? = null

    override fun obtainEvent(viewEvent: EditEvent) {
        when (viewEvent) {
            is EditEvent.SetEditedTodo -> setEditedTodo(viewEvent.todo)
            is EditEvent.ChangeText -> changeText(viewEvent.text)
            is EditEvent.ChangePriority -> changePriority(viewEvent.priority)
            is EditEvent.ChangeDeadline -> changeDeadline(viewEvent.date)
            EditEvent.CheckDeadline -> checkDeadline()
            is EditEvent.SetCalendarShowState -> setCalendarShowState(viewEvent.state)
            EditEvent.ClearViewState -> clearViewState()

        }
    }

    private fun setEditedTodo(todo: TodoItem?) {
        if (todo == null) {
            if (oldTodo != null)
                clearViewState()
        } else {
            oldTodo = todo
            editedTodo = todo.copy()
            deadlineChecked = todo.deadline != null
        }

        viewState.update {
            it.copy(
                editedTodo = editedTodo,
                deadlineChecked = deadlineChecked,
            )
        }
    }

    private fun clearViewState() {
        oldTodo = null
        saveEnable = false
        editedTodo = TodoItem(UUID.randomUUID().toString(), "", TodoPriority.Medium, false, "")
        deadlineChecked = false
        showCalendar = true
        viewState.update { EditViewState() }
    }

    private fun setCalendarShowState(state: Boolean) {
        showCalendar = state

        viewState.update {
            it.copy(showCalendar = showCalendar)
        }
    }

    private fun changeDeadline(date: Long) {
        showCalendar = false
        editedTodo = editedTodo.copy(deadline = date)
        viewState.update {
            it.copy(
                saveEnable = isSaveEnable(), editedTodo = editedTodo, showCalendar = showCalendar
            )
        }
    }

    private fun checkDeadline() {
        deadlineChecked = !deadlineChecked
        if (!deadlineChecked) editedTodo = editedTodo.copy(deadline = null)
        viewState.update {
            it.copy(
                saveEnable = isSaveEnable(),
                deadlineChecked = deadlineChecked,
                editedTodo = editedTodo,
            )
        }
    }

    private fun changeText(text: String) {
        editedTodo = editedTodo.copy(text = text)
        saveEnable = text.isNotBlank()
        viewState.update { it.copy(saveEnable = saveEnable, editedTodo = editedTodo) }
    }

    private fun changePriority(priority: TodoPriority) {
        editedTodo = editedTodo.copy(priority = priority)
        viewState.update { it.copy(saveEnable = isSaveEnable(), editedTodo = editedTodo) }
    }

    private fun isSaveEnable(): Boolean {
        return editedTodo.text.isNotBlank() && editedTodo != oldTodo
    }
}