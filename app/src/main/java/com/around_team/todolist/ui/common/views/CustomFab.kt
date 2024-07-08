package com.around_team.todolist.ui.common.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.ui.theme.TodoListTheme

/**
 * Composable function that displays a custom floating action button (FAB).
 *
 * @param onClick Callback executed when the FAB is clicked.
 * @param modifier Optional [Modifier] for configuring the FAB's layout and behavior.
 */
@Composable
fun CustomFab(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(4.dp, CircleShape)
            .size(44.dp)
            .clip(CircleShape)
            .background(JetTodoListTheme.colors.colors.blue)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = stringResource(id = R.string.add_icon),
            tint = JetTodoListTheme.colors.colors.white
        )
    }
}

/**
 * Preview function for the [CustomFab] composable. Displays the FAB within the TodoListTheme
 * for previewing its appearance.
 */
@Preview
@Composable
private fun CustomButtonPreview() {
    TodoListTheme {
        CustomFab(onClick = {})
    }
}
