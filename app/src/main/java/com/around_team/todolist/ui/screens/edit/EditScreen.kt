package com.around_team.todolist.ui.screens.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.around_team.todolist.R
import com.around_team.todolist.ui.common.enums.TodoPriority
import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.ui.common.views.CustomButton
import com.around_team.todolist.ui.common.views.MyDivider
import com.around_team.todolist.ui.common.views.custom_toolbar.CustomToolbar
import com.around_team.todolist.ui.common.views.custom_toolbar.rememberToolbarScrollBehavior
import com.around_team.todolist.ui.screens.edit.models.EditEvent
import com.around_team.todolist.ui.screens.edit.views.CustomClickableText
import com.around_team.todolist.ui.screens.edit.views.CustomDatePicker
import com.around_team.todolist.ui.screens.edit.views.CustomSwitch
import com.around_team.todolist.ui.screens.edit.views.CustomTabRow
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.utils.formatTimeInMillis

class EditScreen(
    private val viewModel: EditViewModel,
    private val onCancelClick: () -> Unit,
    private val onSaveClick: (editedTodo: TodoItem, delete: Boolean) -> Unit,
    private val editedTodo: TodoItem? = null,
) {
    @Composable
    fun Create() {
        val viewState by viewModel.getViewState().collectAsStateWithLifecycle()
        val scrollBehavior = rememberToolbarScrollBehavior()

        LaunchedEffect(key1 = editedTodo) {
            viewModel.obtainEvent(EditEvent.SetEditedTodo(editedTodo))
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CustomToolbar(
                    navigationIcon = {
                        CustomClickableText(
                            text = stringResource(id = R.string.cancel), onClick = onCancelClick
                        )
                    },
                    collapsingTitle = stringResource(id = R.string.todo),
                    actions = {
                        CustomClickableText(
                            text = stringResource(id = R.string.save),
                            onClick = {
                                onSaveClick(viewState.editedTodo, false)
                                viewModel.obtainEvent(EditEvent.ClearViewState)
                            },
                            fontWeight = FontWeight.Bold, enable = viewState.saveEnable,
                        )
                    },
                    expandedTitleStyle = JetTodoListTheme.typography.headline,
                    collapsedTitleStyle = JetTodoListTheme.typography.headline,
                    changeTitlePosition = false,
                    scrollBehavior = scrollBehavior,
                )
            },
            containerColor = JetTodoListTheme.colors.back.primary,
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                TodoTextField(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = viewState.editedTodo.text,
                    onTextChange = { viewModel.obtainEvent(EditEvent.ChangeText(it)) },
                )

                PriorityAndDatePicker(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    selectedPriority = viewState.editedTodo.priority,
                    onPriorityChanged = { viewModel.obtainEvent(EditEvent.ChangePriority(it)) },
                    checked = viewState.deadlineChecked,
                    onCheckedChange = { viewModel.obtainEvent(EditEvent.CheckDeadline) },
                    selectedDate = viewState.editedTodo.deadline,
                    onDateChange = { viewModel.obtainEvent(EditEvent.ChangeDeadline(it)) },
                    showCalendar = viewState.showCalendar,
                    setCalendarState = { viewModel.obtainEvent(EditEvent.SetCalendarShowState(it)) },
                )
                if (editedTodo != null) {
                    CustomButton(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        text = stringResource(id = R.string.delete),
                        onClick = { onSaveClick(editedTodo, true) },
                        textColor = JetTodoListTheme.colors.colors.red,
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun PriorityAndDatePicker(
        selectedPriority: TodoPriority,
        onPriorityChanged: (TodoPriority) -> Unit,
        checked: Boolean,
        onCheckedChange: () -> Unit,
        selectedDate: Long?,
        onDateChange: (Long) -> Unit,
        showCalendar: Boolean,
        setCalendarState: (state: Boolean) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val state = rememberDatePickerState()
        if (state.selectedDateMillis == null && selectedDate != null) {
            state.setSelection(selectedDate)
        }

        if (checked && state.selectedDateMillis != null && state.selectedDateMillis != selectedDate) {
            LaunchedEffect(key1 = state.selectedDateMillis) {
                //setCalendarState(false)
                onDateChange(state.selectedDateMillis!!)
            }
        }

        Column(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(JetTodoListTheme.colors.back.secondary),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PriorityRow(
                selectedPriority = selectedPriority,
                modifier = Modifier.padding(
                    start = 16.dp, top = 16.dp, end = 16.dp, bottom = 10.dp
                ),
                onPriorityChanged = onPriorityChanged,
            )
            MyDivider(Modifier.padding(start = 16.dp, end = 16.dp, bottom = 10.dp))
            DatePickerRow(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                checked = checked,
                onCheckedChange = {
                    if (!checked) setCalendarState(true)
                    onCheckedChange()
                },
                selectedDate = selectedDate,
                onSelectedDateClick = { setCalendarState(!showCalendar) },
            )
            AnimatedVisibility(visible = checked && showCalendar) {
                CustomDatePicker(
                    modifier = Modifier
                        .offset(y = (-124).dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally), state = state
                )
            }
        }
    }

    @Composable
    private fun PriorityRow(
        selectedPriority: TodoPriority,
        onPriorityChanged: (TodoPriority) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val tabList = TodoPriority.entries.toTypedArray()

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
                selectedTab = selectedPriority.ordinal,
                tabList = tabList,
                onTabChanged = { onPriorityChanged(TodoPriority.getFromOrdinal(it)) },
            ).Create()
        }
    }

    @Composable
    private fun DatePickerRow(
        checked: Boolean,
        onCheckedChange: () -> Unit,
        selectedDate: Long?,
        onSelectedDateClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.deadline),
                    style = JetTodoListTheme.typography.body,
                    color = JetTodoListTheme.colors.label.primary
                )
                if (checked && selectedDate != null) {
                    CustomClickableText(
                        text = formatTimeInMillis(selectedDate),
                        onClick = onSelectedDateClick,
                        style = JetTodoListTheme.typography.footnote
                    )
                }
            }

            CustomSwitch(
                modifier = Modifier.align(Alignment.Top),
                checked = checked,
                onCheckedChange = onCheckedChange,
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
}