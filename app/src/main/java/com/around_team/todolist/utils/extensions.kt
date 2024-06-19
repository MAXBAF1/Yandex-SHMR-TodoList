package com.around_team.todolist.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color

fun Modifier.background(color: () -> Color): Modifier {
    return drawBehind { drawRect(color()) }
}