package com.around_team.todolist.ui.screens.registration

import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.screens.registration.models.RegistrationEvent
import com.around_team.todolist.ui.screens.registration.models.RegistrationViewState
import com.around_team.todolist.utils.PreferencesHelper
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * A ViewModel for managing the state and events of the registration screen.
 */
@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
) : BaseViewModel<RegistrationViewState, RegistrationEvent>(initialState = RegistrationViewState()) {

    override fun obtainEvent(viewEvent: RegistrationEvent) {
        when (viewEvent) {
            is RegistrationEvent.HandleResult -> handleResult(viewEvent.result)
        }
    }

    private fun handleResult(result: YandexAuthResult) {
        when (result) {
            is YandexAuthResult.Success -> onSuccessAuth(result.token)
            is YandexAuthResult.Failure -> onProcessError(result.exception)
            YandexAuthResult.Cancelled -> {}
        }
    }

    private fun onSuccessAuth(token: YandexAuthToken) {
        val tokenValue = token.value
        preferencesHelper.saveToken(tokenValue)
        viewState.update { it.copy(toNextScreen = true) }
    }

    private fun onProcessError(exception: Exception) {
        viewState.update { it.copy(message = exception.message) }
    }
}