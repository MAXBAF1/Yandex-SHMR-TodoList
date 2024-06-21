package com.around_team.todolist.ui.screens.edit.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomSwitch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: () -> Unit,
    width: Dp = 51.dp,
    height: Dp = 31.dp,
    thumbColor: Color = Color(0xFFFFFFFF),
    checkedTrackColor: Color = Color(0xFF35898F),
    uncheckedTrackColor: Color = Color(0xFFe0e0e0),
    gapBetweenThumbAndTrackEdge: Dp = 2.dp,
) {
    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge
    val animatePosition = animateFloatAsState(targetValue = with(LocalDensity.current) {
        if (checked) {
            (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx()
        } else (thumbRadius + gapBetweenThumbAndTrackEdge).toPx()
    }, label = "")
    val bgColor = animateColorAsState(
        targetValue = if (checked) checkedTrackColor else uncheckedTrackColor,
        label = "color animation"
    )

    Canvas(
        modifier = modifier
            .size(width = width, height = height)
            .clickable(
                onClick = onCheckedChange,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ),
    ) {
        drawRoundRect(
            color = bgColor.value,
            cornerRadius = CornerRadius(x = 9999.dp.toPx(), y = 9999.dp.toPx()),
        )
        drawCircle(
            color = thumbColor,
            radius = thumbRadius.toPx(),
            center = Offset(x = animatePosition.value, y = size.height / 2),
        )
    }
}