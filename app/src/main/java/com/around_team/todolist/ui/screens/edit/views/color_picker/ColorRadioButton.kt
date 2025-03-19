package com.around_team.todolist.ui.screens.edit.views.color_picker

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.ui.common.views.CustomIconButton

@Composable
fun ColorRadioButton(
    color: Color,
    modifier: Modifier = Modifier,
    pencilIcon: Boolean = false,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clickable(
                enabled = true,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick
            )
            .size(40.dp)
            .border(2.dp, if (selected) color else Color.Transparent, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        CustomIconButton(
            modifier = Modifier.size(32.dp),
            iconId = if (pencilIcon) R.drawable.ic_pencil else null,
            colors = IconButtonDefaults.iconButtonColors(containerColor = color),
            onClick = onClick,
        )
    }
}