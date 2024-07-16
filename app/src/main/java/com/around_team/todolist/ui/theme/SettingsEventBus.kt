package com.around_team.todolist.ui.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.around_team.todolist.di.SharedPreferencesModule
import com.around_team.todolist.ui.screens.settings.models.ThemeTabs
import com.around_team.todolist.utils.PreferencesHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SettingsEventBus {

    private val _currentSettings = MutableStateFlow(SettingsBundle(isDarkMode = false))
    val currentSettings: StateFlow<SettingsBundle> = _currentSettings

    fun updateDarkMode(isDarkMode: Boolean) {
        _currentSettings.update { it.copy(isDarkMode = isDarkMode) }
    }
}

internal val LocalSettingsEventBus = staticCompositionLocalOf {
    SettingsEventBus()
}