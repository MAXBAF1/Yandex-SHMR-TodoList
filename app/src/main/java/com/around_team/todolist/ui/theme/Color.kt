package com.around_team.todolist.ui.theme

import androidx.compose.ui.graphics.Color

internal val baseLightPalette = JetTodoListColors(
    support = JetTodoListColors.Support(
        separator = Color(0.0F, 0.0F, 0.0F, 0.2F),
        overlay = Color(0.0F, 0.0F, 0.0F, 0.06F),
        navbarBlur = Color(0.98F, 0.98F, 0.98F, 1F),
    ),
    label = JetTodoListColors.Label(
        primary = Color(0.0F, 0.0F, 0.0F, 1.0F),
        secondary = Color(0.0F, 0.0F, 0.0F, 0.6F),
        tertiary = Color(0.0F, 0.0F, 0.0F, 0.3F),
        disable = Color(0.0F, 0.0F, 0.0F, 0.15F),
    ),
    colors = JetTodoListColors.Colors(
        red = Color(1.0F, 0.23F, 0.19F, 1.0F),
        green = Color(0.2F, 0.78F, 0.35F, 1.0F),
        blue = Color(0.0F, 0.48F, 1.0F, 1.0F),
        gray = Color(0.56F, 0.56F, 0.58F, 1.0F),
        grayLight = Color(0.82F, 0.82F, 0.84F, 1.0F),
        white = Color(1.0F, 1.0F, 1.0F, 1.0F),
    ),
    back = JetTodoListColors.Back(
        iosPrimary = Color(0.95F, 0.95F, 0.97F, 1.0F),
        primary = Color(0.97F, 0.97F, 0.95F, 1.0F),
        secondary = Color(1.0F, 1.0F, 1.0F, 1.0F),
        elevated = Color(1.0F, 1.0F, 1.0F, 1.0F),
    )
)

internal val baseDarkPalette = JetTodoListColors(
    support = JetTodoListColors.Support(
        separator = Color(1.0F, 1.0F, 1.0F, 0.2F),
        overlay = Color(0.0F, 0.0F, 0.0F, 0.32F),
        navbarBlur = Color(0.1F, 0.1F, 0.1F, 1F),
    ),
    label = JetTodoListColors.Label(
        primary = Color(1.0F, 1.0F, 1.0F, 1.0F),
        secondary = Color(1.0F, 1.0F, 1.0F, 0.6F),
        tertiary = Color(1.0F, 1.0F, 1.0F, 0.4F),
        disable = Color(1.0F, 1.0F, 1.0F, 0.15F),
    ),
    colors = JetTodoListColors.Colors(
        red = Color(1.0F, 0.27F, 0.23F, 1.0F),
        green = Color(0.2F, 0.84F, 0.29F, 1.0F),
        blue = Color(0.04F, 0.52F, 1.0F, 1.0F),
        gray = Color(0.56F, 0.56F, 0.58F, 1.0F),
        grayLight = Color(0.28F, 0.28F, 0.29F, 1.0F),
        white = Color(1.0F, 1.0F, 1.0F, 1.0F),
    ),
    back = JetTodoListColors.Back(
        iosPrimary = Color(0.0F, 0.0F, 0.0F, 1.0F),
        primary = Color(0.09F, 0.09F, 0.09F, 1.0F),
        secondary = Color(0.14F, 0.14F, 0.16F, 1.0F),
        elevated = Color(0.23F, 0.23F, 0.25F, 1.0F),
    )
)