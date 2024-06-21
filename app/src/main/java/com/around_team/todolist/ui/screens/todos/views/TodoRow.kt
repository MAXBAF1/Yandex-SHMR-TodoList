package com.around_team.todolist.ui.screens.todos.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.ui.common.enums.TodoPriority
import com.around_team.todolist.ui.common.enums.getIconColor
import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.ui.common.views.MyDivider
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.utils.formatTimeInMillis

@Composable
fun TodoRow(
    todo: TodoItem,
    onClick: () -> Unit,
    onCompleteClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(),
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircleCheckbox(
                modifier = Modifier.padding(end = 12.dp),
                checked = todo.completed,
                onChecked = { onCompleteClick(todo.id) },
                highPriority = todo.priority == TodoPriority.High,
            )

            NameAndDateColumn(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1F),
                todo = todo,
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "arrow icon",
                tint = JetTodoListTheme.colors.colors.gray
            )
        }
        if (showDivider) MyDivider()
    }
}

@Composable
private fun NameAndDateColumn(todo: TodoItem, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row {
            if (todo.priority != TodoPriority.Medium) {
                Icon(
                    modifier = Modifier.padding(end = 2.dp),
                    painter = painterResource(id = todo.priority.iconId!!),
                    contentDescription = "priority icon",
                    tint = todo.priority.getIconColor()!!
                )
            }
            Text(
                text = todo.text,
                style = JetTodoListTheme.typography.body,
                color = if (todo.completed) JetTodoListTheme.colors.label.tertiary else JetTodoListTheme.colors.label.primary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                textDecoration = if (todo.completed) TextDecoration.LineThrough else TextDecoration.None
            )
        }

        if (todo.deadline != null) {
            Row(horizontalArrangement = Arrangement.Center) {
                Icon(
                    modifier = Modifier.padding(end = 2.dp),
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "calendar icon",
                    tint = JetTodoListTheme.colors.label.tertiary
                )
                Text(
                    text = formatTimeInMillis(todo.deadline, "d MMMM"),
                    style = JetTodoListTheme.typography.subhead,
                    color = JetTodoListTheme.colors.label.tertiary
                )
            }
        }
    }
}