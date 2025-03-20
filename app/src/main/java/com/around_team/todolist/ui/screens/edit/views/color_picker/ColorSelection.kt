package com.around_team.todolist.ui.screens.edit.views.color_picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.JetTodoListTheme

@Composable
fun ColorSelection(
    colors: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPicker by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.color),
            style = JetTodoListTheme.typography.body,
            color = JetTodoListTheme.colors.label.primary,
        )
        Row {
            colors.forEachIndexed { i, color ->
                ColorRadioButton(
                    modifier = Modifier.padding(end = 2.dp),
                    color = color, selected = color == selectedColor) {
                    onColorSelected(color)
                }
                if (i == colors.size - 1) {
                    val selected = !(colors + Color.Transparent).contains(selectedColor)
                    ColorRadioButton(
                        color = if (selected) selectedColor else Color.Transparent,
                        selected = selected,
                        pencilIcon = true,
                    ) {
                        showPicker = true
                    }
                }
            }
        }
    }

    if (showPicker) {
        ColorPickerPopup(
            onDismiss = { showPicker = false },
            onSave = {
                onColorSelected(it)
                showPicker = false
            },
        )
    }
}
