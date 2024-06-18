package com.around_team.todolist.ui.screens.todos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.around_team.todolist.R
import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.ui.screens.todos.views.TodoRow
import com.around_team.todolist.ui.theme.JetTodoListTheme

class TodosScreen(private val viewModel: TodosViewModel) {

    @Composable
    fun Create() {
        val viewState by viewModel.getViewState().collectAsStateWithLifecycle()

        Surface(
            modifier = Modifier.fillMaxSize(), color = JetTodoListTheme.colors.back.primary
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                CompleteRow(
                    completeCnt = viewState.completeCnt,
                    onShowClick = {},
                    modifier = Modifier.padding(
                        start = 32.dp, top = 0.dp, end = 32.dp, bottom = 12.dp
                    ),
                )
                TodoList(viewState.todos, modifier = Modifier.padding(horizontal = 12.dp))
            }
        }
    }

    @Composable
    private fun TodoList(todos: List<TodoItem>, modifier: Modifier = Modifier) {
        LazyColumn(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(JetTodoListTheme.colors.back.secondary)
                .fillMaxWidth(),
        ) {
            itemsIndexed(todos) { i, todo ->
                TodoRow(todo, onClick = {}, showDivider = i != todos.size - 1)
            }
        }
    }

    @Composable
    private fun CompleteRow(
        completeCnt: Int,
        onShowClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "${stringResource(id = R.string.complete)} $completeCnt",
                style = JetTodoListTheme.typography.subhead,
                color = JetTodoListTheme.colors.label.tertiary
            )
            Text(
                modifier = Modifier.clickable(
                    onClick = onShowClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(),
                ),
                text = stringResource(id = R.string.show),
                style = JetTodoListTheme.typography.subhead,
                fontWeight = FontWeight.Bold,
                color = JetTodoListTheme.colors.colors.blue
            )
        }
    }
}