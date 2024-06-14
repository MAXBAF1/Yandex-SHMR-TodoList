package com.around_team.todolist.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
internal fun TodoListTheme(
    style: JetTodoListStyle = JetTodoListStyle.Base,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = when (darkTheme) {
        true -> {
            when (style) {
                JetTodoListStyle.Base -> baseDarkPalette
            }
        }

        false -> {
            when (style) {
                JetTodoListStyle.Base -> baseLightPalette
            }
        }
    }

    CompositionLocalProvider(
        LocalJetAroundColors provides colors,
        LocalJetTodoListTypography provides typography,
        LocalJetTodoListMargin provides margin,
        content = content
    )
}