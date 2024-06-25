package com.around_team.todolist.ui.screens.edit.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.around_team.todolist.ui.theme.JetTodoListTheme
import kotlinx.coroutines.delay

@Composable
fun CustomClickableText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    style: TextStyle = JetTodoListTheme.typography.body,
    fontWeight: FontWeight? = null,
) {
    var isClicked by remember { mutableStateOf(false) }
    val clickableModifier = if (enable) {
        modifier.clickable(
            onClick = {
                isClicked = true
                onClick()
            },
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
        )
    } else modifier
    val color = when {
        !enable -> JetTodoListTheme.colors.label.tertiary
        isClicked -> JetTodoListTheme.colors.colors.gray
        else -> JetTodoListTheme.colors.colors.blue
    }
    if (isClicked) {
        LaunchedEffect(key1 = Unit) {
            delay(100)
            isClicked = false
        }
    }

    Text(
        modifier = clickableModifier,
        text = text,
        style = style,
        fontWeight = fontWeight,
        color = color
    )
}