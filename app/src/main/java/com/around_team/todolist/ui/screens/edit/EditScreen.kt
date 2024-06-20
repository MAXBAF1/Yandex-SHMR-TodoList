package com.around_team.todolist.ui.screens.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.around_team.todolist.R
import com.around_team.todolist.ui.common.enums.TodoPriority
import com.around_team.todolist.ui.common.views.MyDivider
import com.around_team.todolist.ui.screens.edit.models.EditEvent
import com.around_team.todolist.ui.screens.edit.views.CustomClickableText
import com.around_team.todolist.ui.screens.edit.views.CustomSwitch
import com.around_team.todolist.ui.screens.edit.views.CustomTabRow
import com.around_team.todolist.ui.theme.JetTodoListTheme

class EditScreen(
    private val viewModel: EditViewModel,
    private val onCancelClick: () -> Unit,
    private val onSaveClick: () -> Unit,
) {
    @Composable
    fun Create() {
        val viewState by viewModel.getViewState().collectAsStateWithLifecycle()

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = JetTodoListTheme.colors.back.primary,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                UpperRow(
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(bottom = 16.dp),
                    saveEnable = viewState.saveEnable,
                    onCancelClick = onCancelClick,
                    onSaveClick = onSaveClick
                )
                TodoTextField(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = viewState.editedTodo.text,
                    onTextChange = { viewModel.obtainEvent(EditEvent.ChangeText(it)) },
                )
                PriorityAndDatePickerCard(
                    initialPriority = viewState.editedTodo.priority,
                    onPriorityChanged = { viewModel.obtainEvent(EditEvent.ChangePriority(it)) },
                    checked = viewState.deadlineChecked,
                    onCheckedChange = { viewModel.obtainEvent(EditEvent.CheckDeadline) },
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
            }
        }
    }

    @Composable
    private fun PriorityAndDatePickerCard(
        initialPriority: TodoPriority,
        onPriorityChanged: (TodoPriority) -> Unit,
        checked: Boolean,
        onCheckedChange: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(JetTodoListTheme.colors.back.secondary)
                .padding(16.dp)
        ) {
            PriorityRow(
                initialPriority = initialPriority,
                modifier = Modifier.padding(bottom = 10.dp),
                onPriorityChanged = onPriorityChanged
            )
            MyDivider(Modifier.padding(bottom = 10.dp))
            DatePickerRow(checked = checked, onCheckedChange = onCheckedChange)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun PriorityRow(
        initialPriority: TodoPriority,
        onPriorityChanged: (TodoPriority) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val tabList = TodoPriority.entries.toTypedArray()
        val pagerState = rememberPagerState(initialPriority.ordinal) { tabList.size }

        Row(
            modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1F),
                text = stringResource(id = R.string.priority),
                style = JetTodoListTheme.typography.body,
                color = JetTodoListTheme.colors.label.primary
            )
            CustomTabRow(
                modifier = Modifier.weight(1F),
                pagerState = pagerState,
                tabList = tabList,
                onTabChanged = { onPriorityChanged(TodoPriority.getFromOrdinal(it)) },
            ).Create()
        }
        HorizontalPager(modifier = Modifier.background(Color.Blue), state = pagerState) { }
    }

    @Composable
    private fun DatePickerRow(
        checked: Boolean,
        onCheckedChange: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.deadline),
                style = JetTodoListTheme.typography.body,
                color = JetTodoListTheme.colors.label.primary
            )
            CustomSwitch(
                checked = checked,
                onCheckedChange =                     onCheckedChange
                ,
                thumbColor = JetTodoListTheme.colors.colors.white,
                checkedTrackColor = JetTodoListTheme.colors.colors.green,
                uncheckedTrackColor = JetTodoListTheme.colors.support.overlay
            )
        }
    }

    @Composable
    private fun TodoTextField(
        text: String,
        onTextChange: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        TextField(
            modifier = modifier.fillMaxWidth(),
            value = text,
            onValueChange = onTextChange,
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.what_todo),
                    style = JetTodoListTheme.typography.body,
                    color = JetTodoListTheme.colors.label.tertiary
                )
            },
            textStyle = JetTodoListTheme.typography.body,
            colors = TextFieldDefaults.colors(
                focusedTextColor = JetTodoListTheme.colors.label.primary,
                unfocusedTextColor = JetTodoListTheme.colors.label.primary,
                focusedContainerColor = JetTodoListTheme.colors.back.secondary,
                unfocusedContainerColor = JetTodoListTheme.colors.back.secondary,
                cursorColor = JetTodoListTheme.colors.label.primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            minLines = 6
        )
    }

    @Composable
    private fun UpperRow(
        saveEnable: Boolean,
        onCancelClick: () -> Unit,
        onSaveClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomClickableText(
                text = stringResource(id = R.string.cancel), onClick = onCancelClick
            )
            Text(
                text = stringResource(id = R.string.todo),
                style = JetTodoListTheme.typography.headline,
                color = JetTodoListTheme.colors.label.primary
            )
            CustomClickableText(
                text = stringResource(id = R.string.save),
                onClick = onSaveClick,
                fontWeight = FontWeight.Bold,
                enable = saveEnable
            )
        }
    }
}