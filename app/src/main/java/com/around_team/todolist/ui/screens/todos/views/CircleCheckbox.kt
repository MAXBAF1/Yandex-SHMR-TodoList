package com.around_team.todolist.ui.screens.todos.views

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.JetTodoListTheme

@Composable
fun CircleCheckbox(
    checked: Boolean,
    onChecked: () -> Unit,
    modifier: Modifier = Modifier,
    highPriority: Boolean = false
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
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() },
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = iconId),
            contentDescription = "complete icon",
            tint = tint
        )
    }
}