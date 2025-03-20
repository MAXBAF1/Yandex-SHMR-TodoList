package com.around_team.todolist.ui.common.views

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.ui.theme.TodoListTheme

@Composable
fun CustomIconButton(
    iconId: Int?,
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(contentColor = JetTodoListTheme.colors.label.primary),
    iconSize: Dp = 24.dp,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        colors = colors,
    ) {
        if (iconId != null) {
            Icon(
                modifier = Modifier.size(iconSize),
                painter = painterResource(iconId),
                contentDescription = stringResource(R.string.icon_btn)
            )
        }
    }
}

@Preview
@Composable
private fun CustomIconButtonPreview() {
    TodoListTheme {
        CustomIconButton(iconId = R.drawable.ic_settings, onClick = {})
    }
}
