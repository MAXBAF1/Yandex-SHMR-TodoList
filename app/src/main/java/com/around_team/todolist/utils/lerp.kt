package com.around_team.todolist.utils

import androidx.compose.ui.graphics.Color

fun lerp(a: Float, b: Float, fraction: Float): Float {
    return a + fraction * (b - a)
}

fun lerp(start: Color, stop: Color, fraction: Float): Color {
    return Color(
        red = start.red + fraction * (stop.red - start.red),
        green = start.green + fraction * (stop.green - start.green),
        blue = start.blue + fraction * (stop.blue - start.blue),
        alpha = start.alpha + fraction * (stop.alpha - start.alpha)
    )
}