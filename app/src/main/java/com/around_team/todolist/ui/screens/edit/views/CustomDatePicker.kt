package com.around_team.todolist.ui.screens.edit.views

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.around_team.todolist.ui.theme.JetTodoListTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(state: DatePickerState, modifier: Modifier = Modifier) {
    DatePicker(
        modifier = modifier,
        state = state,
        showModeToggle = false,
        title = { },
        headline = { },
        colors = DatePickerDefaults.colors(
            containerColor = JetTodoListTheme.colors.back.secondary,
            headlineContentColor = JetTodoListTheme.colors.label.primary,
            weekdayContentColor = JetTodoListTheme.colors.label.tertiary,
            yearContentColor = JetTodoListTheme.colors.label.primary,
            currentYearContentColor = JetTodoListTheme.colors.label.primary,
            selectedYearContentColor = JetTodoListTheme.colors.colors.blue,
            selectedYearContainerColor = JetTodoListTheme.colors.colors.blue.copy(0.1F),
            dayContentColor = JetTodoListTheme.colors.label.primary,
            selectedDayContentColor = JetTodoListTheme.colors.colors.blue,
            selectedDayContainerColor = JetTodoListTheme.colors.colors.blue.copy(0.1F),
            todayContentColor = JetTodoListTheme.colors.label.primary,
            todayDateBorderColor = Color.Transparent
        ),
    )
}