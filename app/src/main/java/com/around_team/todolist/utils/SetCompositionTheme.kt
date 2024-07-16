package com.around_team.todolist.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.around_team.todolist.ui.screens.settings.models.ThemeTabs
import com.around_team.todolist.ui.theme.LocalSettingsEventBus

@Composable
fun SetCompositionTheme(selectedTheme: ThemeTabs) {
    val settingsEventBus = LocalSettingsEventBus.current
    val isSystemInDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(selectedTheme) {
        when (selectedTheme) {
            ThemeTabs.Sun -> settingsEventBus.updateDarkMode(false)
            ThemeTabs.Auto -> settingsEventBus.updateDarkMode(isSystemInDarkTheme)
            ThemeTabs.Moon -> settingsEventBus.updateDarkMode(true)
        }
    }
}