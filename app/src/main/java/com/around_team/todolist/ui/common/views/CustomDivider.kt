package com.around_team.todolist.ui.common.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.ui.theme.TodoListTheme

/**
 * Composable function that displays a horizontal divider with a specified thickness and color.
 *
 * @param modifier Optional [Modifier] for configuring the divider's layout and behavior.
 */
@Composable
fun MyDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 0.5.dp,
        color = JetTodoListTheme.colors.support.separator
    )
}

/**
 * Preview function for the [MyDivider] composable. Displays the divider within a Box with background
 * and padding to showcase its appearance.
 */
@Preview
@Composable
private fun CustomButtonPreview() {
    TodoListTheme {
        Box(modifier = Modifier.background(JetTodoListTheme.colors.back.primary).padding(10.dp)) {
            MyDivider()
        }
    }
}
