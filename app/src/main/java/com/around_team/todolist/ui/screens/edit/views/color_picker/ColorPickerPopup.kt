package com.around_team.todolist.ui.screens.edit.views.color_picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.ui.common.views.CustomButton
import com.around_team.todolist.ui.theme.JetTodoListTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerPopup(
    onDismiss: () -> Unit,
    onSave: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    var brightness by remember { mutableFloatStateOf(1f) }
    var selectedColor by remember { mutableStateOf(Color.Blue) }

    BasicAlertDialog(
        modifier = modifier.background(Color.White, shape = RoundedCornerShape(16.dp)),
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ColorPicker(
                onColorChange = { selectedColor = it },
                brightness = brightness,
                boxWidth = 300.dp,
                boxHeight = 150.dp,
            )
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = stringResource(id = R.string.brightness),
                    style = JetTodoListTheme.typography.body,
                    color = JetTodoListTheme.colors.label.primary
                )
                Slider(value = brightness, onValueChange = { brightness = it })
            }
            Button(
                onClick = { onSave(selectedColor) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = stringResource(id = R.string.save),
                    style = JetTodoListTheme.typography.body,
                    color = JetTodoListTheme.colors.label.primary
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(selectedColor, shape = CircleShape)
                )
            }
        }
    }
}
