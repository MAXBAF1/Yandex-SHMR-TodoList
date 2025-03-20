package com.around_team.todolist.ui.screens.edit.views.color_picker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R

@Composable
fun ColorRadioButton(
    color: Color,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    pencilIcon: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick
            )
            .border(2.dp, if (selected) color else Color.Transparent, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center,
        ) {
            if (pencilIcon) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(R.drawable.ic_pencil),
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview
@Composable
fun CustomRadioButtonPreview() {
    ColorRadioButton(
        color = Color.Blue, selected = true, pencilIcon = true
    ) {}
}


@Preview
@Composable
fun CustomRadioButtonPreview1() {
    ColorRadioButton(
        color = Color.Blue, selected = true, pencilIcon = false
    ) {}
}
