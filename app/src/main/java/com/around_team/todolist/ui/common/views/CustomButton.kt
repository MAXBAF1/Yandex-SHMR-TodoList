package com.around_team.todolist.ui.common.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.around_team.todolist.ui.theme.JetTodoListTheme

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    Button(
        modifier = modifier.fillMaxWidth().shadow(2.dp, shape),
        onClick = onClick,
        shape = shape,
        contentPadding = PaddingValues(vertical = 17.dp),
        colors = ButtonDefaults.buttonColors(containerColor = JetTodoListTheme.colors.back.secondary)
    ) {
        Text(text = text, style = JetTodoListTheme.typography.body, color = textColor)
    }
}