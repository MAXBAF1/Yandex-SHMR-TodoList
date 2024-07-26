package com.around_team.todolist.ui.common.views

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.around_team.todolist.ui.theme.JetTodoListTheme

@Composable
fun CustomTextButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(modifier = modifier, onClick = onClick) {
        Text(
            text = text,
            style = JetTodoListTheme.typography.subhead,
            fontWeight = FontWeight.Bold,
            color = JetTodoListTheme.colors.colors.blue
        )
    }
}