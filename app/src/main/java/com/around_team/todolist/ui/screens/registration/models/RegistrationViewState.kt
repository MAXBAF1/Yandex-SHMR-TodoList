package com.around_team.todolist.ui.screens.registration.models

/**
 * A data class representing the state of the registration view.
 */
data class RegistrationViewState(
    val message: String? = null,
    val toNextScreen: Boolean = false,
)