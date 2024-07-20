package com.around_team.todolist.ui.screens.settings.models


sealed class SettingsEvent {
    data class ThemeChanged(val theme: Theme): SettingsEvent()
}