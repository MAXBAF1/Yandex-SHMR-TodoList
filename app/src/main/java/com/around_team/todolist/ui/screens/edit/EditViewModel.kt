package com.around_team.todolist.ui.screens.edit

import androidx.compose.ui.graphics.Color
import com.around_team.todolist.data.network.repositories.Repository
import com.around_team.todolist.ui.common.enums.TodoImportance
import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.ui.screens.edit.models.EditEvent
import com.around_team.todolist.ui.screens.edit.models.EditViewState
import com.around_team.todolist.utils.PreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import java.util.Date
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for managing the state and business logic related to editing or creating a todo item.
 *
 * This ViewModel extends [BaseViewModel] and manages [EditViewState] and [EditEvent].
 *
 * @param repository The repository used for accessing and manipulating todo item data.
 * @param preferencesHelper Helper class for managing application preferences.
 */
@HiltViewModel
class EditViewModel @Inject constructor(
    private val repository: Repository,
    private val preferencesHelper: PreferencesHelper,
) : BaseViewModel<EditViewState, EditEvent>(initialState = EditViewState()) {
    private var saveEnable: Boolean = false
    private var editedTodo: TodoItem = TodoItem(
        id = UUID
            .randomUUID()
            .toString(),
        text = "",
        importance = TodoImportance.Basic,
        done = false,
        creationDate = Date().time
    )
    private var deadlineChecked: Boolean = false
    private var showCalendar: Boolean = true
    private var oldTodo: TodoItem? = null

    override fun obtainEvent(viewEvent: EditEvent) {
        when (viewEvent) {
            is EditEvent.SetEditedTodo -> setEditedTodo(viewEvent.todoId)
            is EditEvent.ChangeText -> changeText(viewEvent.text)
            is EditEvent.ChangePriority -> changePriority(viewEvent.priority)
            is EditEvent.ChangeDeadline -> changeDeadline(viewEvent.date)
            EditEvent.CheckDeadline -> checkDeadline()
            is EditEvent.SetCalendarShowState -> setCalendarShowState(viewEvent.state)
            EditEvent.SaveTodo -> saveTodo()
            EditEvent.DeleteTodo -> deleteTodo()
            EditEvent.ClearViewState -> clearViewState()
            is EditEvent.ChangeColor -> changeColor(viewEvent.color)
        }
    }

    private fun changeColor(color: Color) {
        viewState.update { it.copy(selectedColor = color) }
    }

    private fun saveTodo() {
        editedTodo = editedTodo.copy(
            modifiedDate = Date().time, lastUpdatedBy = preferencesHelper.getUUID()
        )
        if (oldTodo == null) repository.saveTodo(editedTodo) else repository.updateTodo(editedTodo)
        viewState.update { it.copy(toTodosScreen = true) }
    }

    private fun deleteTodo() {
        repository.deleteTodo(editedTodo.id)
        viewState.update { it.copy(toTodosScreen = true) }
    }

    private fun clearViewState() {
        oldTodo = null
        saveEnable = false
        editedTodo = TodoItem(
            id = UUID
                .randomUUID()
                .toString(),
            text = "",
            importance = TodoImportance.Basic,
            done = false,
            creationDate = Date().time
        )
        deadlineChecked = false
        showCalendar = true
        viewState.update { EditViewState() }
    }

    private fun setEditedTodo(id: String?) {
        if (id == null) {
            if (oldTodo != null) clearViewState()
        } else {
            val todo = repository.getTodoById(id) ?: return
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

    private fun setCalendarShowState(state: Boolean) {
        showCalendar = state

        viewState.update { it.copy(showCalendar = showCalendar) }
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

    private fun changePriority(priority: TodoImportance) {
        editedTodo = editedTodo.copy(importance = priority)
        viewState.update { it.copy(saveEnable = isSaveEnable(), editedTodo = editedTodo) }
    }

    private fun isSaveEnable(): Boolean {
        return editedTodo.text.isNotBlank() && editedTodo != oldTodo
    }
}