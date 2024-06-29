package com.around_team.todolist.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

data class JetTodoListColors(
    val support: Support,
    val label: Label,
    val colors: Colors,
    val back: Back,
) {
    data class Support(
        val separator: Color,
        val overlay: Color,
        val navbarBlur: Color,
    )

    data class Label(
        val primary: Color,
        val secondary: Color,
        val tertiary: Color,
        val disable: Color,
    )

    data class Colors(
        val red: Color,
        val green: Color,
        val blue: Color,
        val gray: Color,
        val grayLight: Color,
        val white: Color,
    )

    data class Back(
        val iosPrimary: Color,
        val primary: Color,
        val secondary: Color,
        val elevated: Color,
    )
}

data class JetTodoListTypography(
    val largeTitle: TextStyle,
    val collapsedLargeTitle: TextStyle,
    val title: TextStyle,
    val headline: TextStyle,
    val body: TextStyle,
    val subhead: TextStyle,
    val footnote: TextStyle,
)

enum class JetTodoListStyle {
    Base
}

object JetTodoListTheme {
    internal val colors: JetTodoListColors
        @Composable get() = LocalJetAroundColors.current

    internal val typography: JetTodoListTypography
        @Composable get() = LocalJetTodoListTypography.current
}

internal val LocalJetAroundColors = staticCompositionLocalOf<JetTodoListColors> {
    error("No colors provided")
}

internal val LocalJetTodoListTypography = staticCompositionLocalOf<JetTodoListTypography> {
    error("No font provided")
}