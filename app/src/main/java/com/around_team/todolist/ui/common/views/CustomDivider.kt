package com.around_team.todolist.ui.common.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.ui.theme.TodoListTheme

@Composable
fun MyDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier, thickness = 0.5.dp, color = JetTodoListTheme.colors.support.separator
    )
}

@Preview
@Composable
private fun CustomButtonPreview() {
    TodoListTheme {
        Box(modifier = Modifier.background(JetTodoListTheme.colors.back.primary).padding(10.dp)) {
            MyDivider()
        }
    }
}