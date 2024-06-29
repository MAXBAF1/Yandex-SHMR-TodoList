package com.around_team.todolist.ui.screens.todos.views

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.data.model.TodoItem
import com.around_team.todolist.ui.common.enums.TodoImportance
import com.around_team.todolist.ui.common.enums.getIconColor
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.utils.FormatTimeInMillis
import java.util.Date
import java.util.UUID

@Composable
fun TodoCard(todo: TodoItem, onClick: () -> Unit, onCompleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
            )
            .background(JetTodoListTheme.colors.back.secondary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleCheckbox(
            modifier = Modifier.padding(end = 12.dp),
            checked = todo.done,
            onChecked = onCompleteClick,
            highPriority = todo.importance == TodoImportance.Important,
        )

        NameAndDateColumn(
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1F),
            todo = todo,
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow),
            contentDescription = stringResource(id = R.string.arrow_icon),
            tint = JetTodoListTheme.colors.colors.gray
        )
    }
}


@Composable
private fun NameAndDateColumn(todo: TodoItem, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (todo.importance != TodoImportance.Basic) {
                Icon(
                    modifier = Modifier.padding(end = 2.dp),
                    painter = painterResource(id = todo.importance.iconId!!),
                    contentDescription = stringResource(id = R.string.priority_icon),
                    tint = todo.importance.getIconColor()
                )
            }
            Text(
                text = todo.text,
                style = JetTodoListTheme.typography.body,
                color = if (todo.done) JetTodoListTheme.colors.label.tertiary else JetTodoListTheme.colors.label.primary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                textDecoration = if (todo.done) TextDecoration.LineThrough else TextDecoration.None
            )
        }

        if (todo.deadline != null) {
            Row(horizontalArrangement = Arrangement.Center) {
                Icon(
                    modifier = Modifier.padding(end = 2.dp),
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = stringResource(id = R.string.calendar_icon),
                    tint = JetTodoListTheme.colors.label.tertiary
                )
                Text(
                    text = FormatTimeInMillis.format(todo.deadline, "d MMMM"),
                    style = JetTodoListTheme.typography.subhead,
                    color = JetTodoListTheme.colors.label.tertiary
                )
            }
        }
    }
}

@Preview
@Composable
private fun TodoCardPreview() {
    TodoCard(TodoItem(
        id = UUID.randomUUID().toString(),
        text = "",
        importance = TodoImportance.Basic,
        done = false,
        creationDate = Date().time
    ), onClick = {}, onCompleteClick = {})
}