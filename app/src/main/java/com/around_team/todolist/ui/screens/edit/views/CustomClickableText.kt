package com.around_team.todolist.ui.screens.edit.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.ui.theme.TodoListTheme
import kotlinx.coroutines.delay

/**
 * A composable function that displays clickable text.
 *
 * @param text The text to display.
 * @param onClick The callback to be invoked when the text is clicked.
 * @param modifier Optional modifier for styling or positioning the text.
 * @param enable Boolean flag indicating whether the text is clickable.
 * @param style The style to apply to the text.
 * @param fontWeight Optional font weight to apply to the text.
 */
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
    val clickableModifier = modifier.clickable(
        onClick = {
            isClicked = true
            onClick()
        },
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enable
    )

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

    Box(modifier = clickableModifier.semantics { role = Role.Button }) {
        Text(
            text = text, style = style, fontWeight = fontWeight, color = color
        )
    }
}

@Preview
@Composable
private fun CustomClickableTextPreview() {
    TodoListTheme {
        CustomClickableText(text = stringResource(id = R.string.what_todo), onClick = { })
    }
}