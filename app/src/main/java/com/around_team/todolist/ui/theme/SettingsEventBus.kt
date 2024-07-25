package com.around_team.todolist.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SettingsEventBus {

    private val _currentSettings: MutableStateFlow<SettingsBundle?> = MutableStateFlow(null)
    val currentSettings: StateFlow<SettingsBundle?> = _currentSettings

    fun updateDarkMode(isDarkMode: Boolean) {
        _currentSettings.update {
            it?.copy(isDarkMode = isDarkMode) ?: SettingsBundle(isDarkMode = isDarkMode)
        }
    }
}

internal val LocalSettingsEventBus = staticCompositionLocalOf {
    SettingsEventBus()
}