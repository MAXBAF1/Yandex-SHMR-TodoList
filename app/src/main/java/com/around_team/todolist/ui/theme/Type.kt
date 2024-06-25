package com.around_team.todolist.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.around_team.todolist.R

val typography = JetTodoListTypography(
    largeTitle = TextStyle(
        fontFamily = FontFamily(Font(R.font.sf_pro_display_bold)),
        fontSize = 38.sp,
        lineHeight = 46.sp,
        letterSpacing = 0.42.sp
    ),
    collapsedLargeTitle = TextStyle(
        fontFamily = FontFamily(Font(R.font.sf_pro_display_bold)),
        fontSize = 38.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.42.sp
    ),
    title = TextStyle(
        fontFamily = FontFamily(Font(R.font.sf_pro_display_semibold)),
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.38.sp
    ),
    headline = TextStyle(
        fontFamily = FontFamily(Font(R.font.sf_pro_text_semibold)),
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.41).sp
    ),
    body = TextStyle(
        fontFamily = FontFamily(Font(R.font.sf_pro_text_regular)),
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.41).sp
    ),
    subhead = TextStyle(
        fontFamily = FontFamily(Font(R.font.sf_pro_text_regular)),
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.24).sp
    ),
    footnote = TextStyle(
        fontFamily = FontFamily(Font(R.font.sf_pro_text_semibold)),
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.08).sp
    ),
)