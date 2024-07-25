package com.around_team.todolist.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.around_team.todolist.ui.screens.settings.models.Theme
import com.around_team.todolist.ui.theme.LocalSettingsEventBus

@Composable
fun SetCompositionTheme(selectedTheme: Theme) {
    val settingsEventBus = LocalSettingsEventBus.current
    val isSystemInDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(selectedTheme) {
        when (selectedTheme) {
            Theme.Sun -> settingsEventBus.updateDarkMode(false)
            Theme.Auto -> settingsEventBus.updateDarkMode(isSystemInDarkTheme)
            Theme.Moon -> settingsEventBus.updateDarkMode(true)
        }
    }
}