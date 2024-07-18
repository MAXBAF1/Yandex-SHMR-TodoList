package com.around_team.todolist.ui.screens.edit.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.TodoListTheme

/**
 * A composable function that displays a custom switch component using Jetpack Compose.
 *
 * @param checked The current checked state of the switch.
 * @param onCheckedChange Lambda that will be called when the switch state changes.
 * @param modifier Optional modifier for styling or positioning the switch.
 * @param width The width of the switch.
 * @param height The height of the switch.
 * @param thumbColor The color of the switch's thumb.
 * @param checkedTrackColor The color of the track when the switch is checked.
 * @param uncheckedTrackColor The color of the track when the switch is unchecked.
 * @param gapBetweenThumbAndTrackEdge The gap between the thumb and the edges of the track.
 */
@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier,
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
        label = stringResource(R.string.color_animation)
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

@Preview
@Composable
private fun CustomSwitchPreview() {
    TodoListTheme {
        CustomSwitch(checked = true, onCheckedChange = { })
    }
}