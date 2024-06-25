package com.around_team.todolist.ui.common.views

import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.around_team.todolist.ui.theme.JetTodoListTheme

@Composable
fun MyDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier,
        thickness = 0.5.dp,
        color = JetTodoListTheme.colors.support.separator
    )
}