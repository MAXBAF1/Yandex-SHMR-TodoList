package com.around_team.todolist.ui.screens.settings

import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.screens.settings.models.SettingsEvent
import com.around_team.todolist.ui.screens.settings.models.SettingsViewState
import com.around_team.todolist.ui.screens.settings.models.Theme
import com.around_team.todolist.utils.PreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
) : BaseViewModel<SettingsViewState, SettingsEvent>(initialState = SettingsViewState()) {
    private var selectedTheme: Theme = Theme.Auto

    init {
        selectedTheme = preferencesHelper.getSelectedTheme() ?: Theme.Auto
        viewState.update { it.copy(selectedTheme = selectedTheme) }
    }

    override fun obtainEvent(viewEvent: SettingsEvent) {
        when (viewEvent) {
            is SettingsEvent.ThemeChanged -> changeTheme(viewEvent.theme)
        }
    }

    private fun changeTheme(theme: Theme) {
        preferencesHelper.saveSelectedTheme(theme)
        viewState.update { it.copy(selectedTheme = theme) }
    }
}