package com.around_team.todolist.ui.common.views

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.JetTodoListTheme

@Composable
fun CustomIconButton(
    iconId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = JetTodoListTheme.colors.label.primary,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(contentColor = color)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(iconId),
            contentDescription = stringResource(R.string.icon_btn)
        )
    }
}