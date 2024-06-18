package com.around_team.todolist.ui.screens.todos.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.around_team.todolist.ui.theme.JetTodoListTheme

@Composable
fun CircleCheckbox(
    checked: Boolean,
    onChecked: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = JetTodoListTheme.colors
    val imageVector = if (checked) Icons.Filled.CheckCircle else Icons.Outlined.Circle
    val tint = if (checked) colors.colors.green else colors.support.separator
    val background = if (checked) Color.Transparent else Color.Transparent

    IconButton(
        onClick = { onChecked() },
        modifier = modifier,
        enabled = enabled,
    ) {
        Icon(
            modifier = Modifier.background(background, shape = CircleShape),
            imageVector = imageVector,
            tint = tint,
            contentDescription = "checkbox",
        )
    }
}