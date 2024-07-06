package com.around_team.todolist.ui.common.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Abstract base class for ViewModels that manage a state and handle events.
 *
 * @param State The type representing the state of the ViewModel.
 * @param Event The type representing events that can be processed by the ViewModel.
 * @property initialState The initial state of the ViewModel.
 */
abstract class BaseViewModel<State, Event>(initialState: State) : ViewModel() {

    /**
     * Mutable state flow to hold the current state of the ViewModel.
     */
    protected val viewState = MutableStateFlow(initialState)

    /**
     * Retrieves the current state as a [StateFlow], which can be observed by the UI.
     *
     * @return The [StateFlow] representing the current state.
     */
    fun getViewState(): StateFlow<State> = viewState

    /**
     * Method to be overridden by subclasses to handle incoming events.
     *
     * @param viewEvent The event received by the ViewModel.
     */
    open fun obtainEvent(viewEvent: Event) {
        // Default implementation does nothing; subclasses should override this method.
    }
}