package com.around_team.todolist.ui.screens.edit

import com.around_team.todolist.ui.common.enums.TodoPriority
import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.ui.screens.edit.models.EditEvent
import com.around_team.todolist.ui.screens.edit.models.EditViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor() :
    BaseViewModel<EditViewState, EditEvent>(initialState = EditViewState()) {
    private var saveEnable: Boolean = false
    private var editedTodo: TodoItem = TodoItem("", "", TodoPriority.Medium, false, "")
    private var deadlineChecked: Boolean = false
    private var showCalendar: Boolean = true

    override fun obtainEvent(viewEvent: EditEvent) {
        when (viewEvent) {
            is EditEvent.ChangeText -> changeText(viewEvent.text)
            is EditEvent.ChangePriority -> changePriority(viewEvent.priority)
            is EditEvent.ChangeDeadline -> changeDeadline(viewEvent.date)
            EditEvent.CheckDeadline -> checkDeadline()
            is EditEvent.SetCalendarShowState -> setCalendarShowState(viewEvent.state)
        }
    }

    private fun setCalendarShowState(state: Boolean) {
        showCalendar = state
        viewState.update { it.copy(showCalendar = showCalendar) }
    }

    private fun changeDeadline(date: Long) {
        editedTodo = editedTodo.copy(deadline = date)
        viewState.update { it.copy(editedTodo = editedTodo) }
    }

    private fun checkDeadline() {
        deadlineChecked = !deadlineChecked
        viewState.update { it.copy(deadlineChecked = deadlineChecked) }
    }

    private fun changeText(text: String) {
        editedTodo = editedTodo.copy(text = text)
        saveEnable = text.isNotBlank()
        viewState.update { it.copy(saveEnable = saveEnable, editedTodo = editedTodo) }
    }

    private fun changePriority(priority: TodoPriority) {
        editedTodo = editedTodo.copy(priority = priority)
        viewState.update { it.copy(editedTodo = editedTodo) }
    }
}