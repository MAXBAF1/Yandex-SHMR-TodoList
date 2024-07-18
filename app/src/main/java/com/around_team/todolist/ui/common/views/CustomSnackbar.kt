package com.around_team.todolist.ui.common.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.ui.theme.TodoListTheme

/**
 * Composable function that displays a custom Snackbar using Jetpack Compose.
 */
@Composable
fun CustomSnackbar(
    message: String,
    action: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = JetTodoListTheme.colors.back.primary),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(vertical = 16.dp).weight(1F),
                text = message,
                style = JetTodoListTheme.typography.subhead,
                color = JetTodoListTheme.colors.label.primary
            )
            TextButton(modifier = Modifier.padding(end = 4.dp), onClick = onActionClick) {
                Text(
                    text = action,
                    style = JetTodoListTheme.typography.subhead,
                    fontWeight = FontWeight.Bold,
                    color = JetTodoListTheme.colors.colors.blue
                )
            }
        }
    }
}

/**
 * Preview function for the [CustomSnackbar] composable. Displays the Snackbar within the
 * TodoListTheme for previewing its appearance.
 */
@Preview
@Composable
private fun CustomSnackbarPreview() {
    TodoListTheme {
        CustomSnackbar(message = stringResource(R.string.todo_deleted),
            action = stringResource(R.string.cancel),
            onActionClick = {})
    }
}
