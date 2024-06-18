package com.around_team.todolist.ui.screens.todos

import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.ui.screens.todos.models.TodosEvent
import com.around_team.todolist.ui.screens.todos.models.TodosViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
) : BaseViewModel<TodosViewState, TodosEvent>(initialState = TodosViewState()) {
    private val todos: List<TodoItem> = listOf()
    private val completeCtn = 0

    init {

    }

    override fun obtainEvent(viewEvent: TodosEvent) {
        /*when (viewEvent) {
            TodosEvent.ExitFromAccount -> exitFromAccount()
        }*/
    }

}