package com.around_team.todolist.ui.screens.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.around_team.todolist.ui.theme.JetTodoListTheme

class EditScreen {
    @Composable
    fun Create() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = JetTodoListTheme.colors.back.primary,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

            }
        }
    }
}