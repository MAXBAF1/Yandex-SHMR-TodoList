package com.around_team.todolist.ui.screens.todos.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.ui.theme.TodoListTheme

/**
 * Composable representing a circular checkbox.
 *
 * @param checked Boolean indicating whether the checkbox is checked.
 * @param onChecked Callback function invoked when the checkbox is toggled.
 * @param modifier Optional [Modifier] that can be used to adjust layout or appearance.
 * @param highPriority Boolean indicating whether the checkbox represents a high priority item.
 */
@Composable
fun CircleCheckbox(
    checked: Boolean,
    onChecked: () -> Unit,
    modifier: Modifier = Modifier,
    highPriority: Boolean = false,
) {
    val colors = JetTodoListTheme.colors
    val iconId = if (checked) R.drawable.ic_complete else R.drawable.ic_circle
    val tint = when {
        checked -> colors.colors.green
        highPriority -> colors.colors.red
        else -> colors.support.separator
    }
    val bgColor = if (highPriority && !checked) colors.colors.red.copy(0.1F) else Color.Transparent

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(bgColor)
            .clickable(
                onClick = onChecked,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ), contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = iconId),
            contentDescription = stringResource(id = R.string.complete_icon),
            tint = tint
        )
    }
}

@Preview
@Composable
private fun CircleCheckboxPreview() {
    TodoListTheme {
        CircleCheckbox(checked = true, onChecked = {})
    }
}