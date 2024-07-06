package com.around_team.todolist.ui.screens.todos.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.ui.theme.TodoListTheme

/**
 * Composable function to create a new card with an "Add" icon and text.
 *
 * @param onClick Callback function invoked when the card is clicked.
 * @param modifier Optional [Modifier] that can be used to adjust layout or appearance.
 */
@Composable
fun CreateNewCard(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
            )
            .background(JetTodoListTheme.colors.back.secondary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(end = 12.dp)
                .clip(CircleShape)
                .background(JetTodoListTheme.colors.colors.blue)
                .size(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(12.dp),
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "add icon",
                tint = JetTodoListTheme.colors.colors.white
            )
        }

        Text(
            text = stringResource(id = R.string.new_todo),
            style = JetTodoListTheme.typography.body,
            color = JetTodoListTheme.colors.colors.blue
        )
    }
}

@Preview
@Composable
private fun CreateNewCardPreview() {
    TodoListTheme {
        CreateNewCard(onClick = {})
    }
}