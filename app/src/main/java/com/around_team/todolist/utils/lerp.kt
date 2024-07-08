package com.around_team.todolist.utils

import androidx.compose.ui.graphics.Color

/**
 * Linearly interpolates between two float values.
 *
 * @param a The starting value.
 * @param b The ending value.
 * @param fraction The fraction of the distance from a to b to interpolate.
 *                 Should be between 0 and 1.
 * @return The interpolated value between a and b.
 */
fun lerp(a: Float, b: Float, fraction: Float): Float {
    return a + fraction * (b - a)
}


/**
 * Linearly interpolates between two Color values.
 *
 * @param start The starting Color.
 * @param stop The ending Color.
 * @param fraction The fraction of the distance from start to stop to interpolate.
 *                 Should be between 0 and 1.
 * @return The interpolated Color between start and stop.
 */
fun lerp(start: Color, stop: Color, fraction: Float): Color {
    return Color(
        red = start.red + fraction * (stop.red - start.red),
        green = start.green + fraction * (stop.green - start.green),
        blue = start.blue + fraction * (stop.blue - start.blue),
        alpha = start.alpha + fraction * (stop.alpha - start.alpha)
    )
}
