package com.around_team.todolist.ui.common.views

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.ui.theme.TodoListTheme

/**
 * Composable function that displays a custom Snackbar using Jetpack Compose.
 *
 * @param hostState [SnackbarHostState] object that manages the state of the Snackbar.
 */
@Composable
fun CustomSnackbar(hostState: SnackbarHostState) {
    SnackbarHost(hostState = hostState) {
        Snackbar(
            snackbarData = it,
            shape = RoundedCornerShape(16.dp),
            containerColor = JetTodoListTheme.colors.back.primary,
            contentColor = JetTodoListTheme.colors.label.primary,
            actionColor = JetTodoListTheme.colors.colors.blue,
        )
    }
}

/**
 * Preview function for the [CustomSnackbar] composable. Displays the Snackbar within the
 * TodoListTheme for previewing its appearance.
 */
@Preview
@Composable
private fun CustomSnackbarPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    TodoListTheme {
        CustomSnackbar(snackbarHostState)
    }
}
