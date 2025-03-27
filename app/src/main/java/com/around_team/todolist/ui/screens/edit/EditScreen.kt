package com.around_team.todolist.ui.screens.edit

import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.around_team.todolist.R
import com.around_team.todolist.data.db.DatabaseRepository
import com.around_team.todolist.data.network.RequestManager
import com.around_team.todolist.data.network.repositories.Repository
import com.around_team.todolist.ui.common.enums.TodoImportance
import com.around_team.todolist.ui.common.views.CustomButton
import com.around_team.todolist.ui.common.views.MyDivider
import com.around_team.todolist.ui.common.views.custom_toolbar.CustomToolbar
import com.around_team.todolist.ui.common.views.custom_toolbar.rememberToolbarScrollBehavior
import com.around_team.todolist.ui.screens.edit.models.EditEvent
import com.around_team.todolist.ui.screens.edit.views.CustomClickableText
import com.around_team.todolist.ui.screens.edit.views.CustomDatePicker
import com.around_team.todolist.ui.screens.edit.views.CustomSwitch
import com.around_team.todolist.ui.common.views.CustomTabRow
import com.around_team.todolist.ui.screens.edit.views.color_picker.ColorSelection
import com.around_team.todolist.ui.screens.todos.testDao
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.ui.theme.TodoListTheme
import com.around_team.todolist.utils.FormatTimeInMillis
import com.around_team.todolist.utils.PreferencesHelper

/**
 * Represents the Edit screen for editing or creating a new todo item.
 *
 * @param viewModel The view model associated with this screen, providing access to business logic and state management.
 * @param onCancelClick Lambda function invoked when the cancel button is clicked, typically used to navigate back or dismiss the screen.
 * @param toTodosScreen Lambda function invoked to navigate back to the Todos screen after saving or canceling the edit operation.
 * @param editedTodoId Optional ID of the todo item being edited, if provided, otherwise indicates a new todo item creation.
 */

@Composable
fun EditScreen(
    onCancelClick: () -> Unit,
    toTodosScreen: () -> Unit,
    modifier: Modifier = Modifier,
    editedTodoId: String? = null,
    viewModel: EditViewModel = hiltViewModel(),
) {
    val viewState by viewModel.getViewState().collectAsStateWithLifecycle()
    val scrollBehavior = rememberToolbarScrollBehavior()

    LaunchedEffect(key1 = editedTodoId) {
        viewModel.obtainEvent(EditEvent.SetEditedTodo(editedTodoId))
    }

    if (viewState.toTodosScreen) {
        toTodosScreen()
        viewModel.obtainEvent(EditEvent.ClearViewState)
    }

    Scaffold(
        modifier = modifier
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
                            viewModel.obtainEvent(EditEvent.SaveTodo)
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
                selectedPriority = viewState.editedTodo.importance,
                onPriorityChanged = { viewModel.obtainEvent(EditEvent.ChangePriority(it)) },
                checked = viewState.deadlineChecked,
                onCheckedChange = { viewModel.obtainEvent(EditEvent.CheckDeadline) },
                selectedDate = viewState.editedTodo.deadline,
                onDateChange = { viewModel.obtainEvent(EditEvent.ChangeDeadline(it)) },
                showCalendar = viewState.showCalendar,
                setCalendarState = { viewModel.obtainEvent(EditEvent.SetCalendarShowState(it)) },
                selectedColor = viewState.selectedColor,
                onColorSelected = { viewModel.obtainEvent(EditEvent.ChangeColor(it)) },
            )
            AnimatedVisibility(visible = editedTodoId != null) {
                CustomButton(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = stringResource(id = R.string.delete),
                    onClick = { viewModel.obtainEvent(EditEvent.DeleteTodo) },
                    textColor = JetTodoListTheme.colors.colors.red,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PriorityAndDatePicker(
    selectedPriority: TodoImportance,
    onPriorityChanged: (TodoImportance) -> Unit,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    selectedDate: Long?,
    onDateChange: (Long) -> Unit,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    showCalendar: Boolean,
    setCalendarState: (state: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberDatePickerState()
    if (state.selectedDateMillis == null && selectedDate != null) {
        state.selectedDateMillis = selectedDate
    }

    if (checked && state.selectedDateMillis != null && state.selectedDateMillis != selectedDate) {
        LaunchedEffect(key1 = state.selectedDateMillis) {
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
        MyDivider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 10.dp))
        ColorSelection(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
            colors = listOf(Color.Yellow, Color.Red, Color.Green, Color.Cyan),
            selectedColor = selectedColor,
            onColorSelected = onColorSelected,
        )
        MyDivider(Modifier.padding(start = 16.dp, end = 16.dp, bottom = 10.dp))
        DatePickerRow(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            checked = checked,
            onCheckedChange = {
                if (!checked) setCalendarState(true) else state.selectedDateMillis = null
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
                    .align(Alignment.CenterHorizontally),
                state = state,
            )
        }
    }
}

@Composable
private fun PriorityRow(
    selectedPriority: TodoImportance,
    onPriorityChanged: (TodoImportance) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabList = TodoImportance.entries

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
            onTabChanged = { onPriorityChanged(TodoImportance.getFromOrdinal(it)) },
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
            AnimatedVisibility(visible = checked && selectedDate != null) {
                CustomClickableText(
                    text = FormatTimeInMillis.format(selectedDate),
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


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun EditScreenPreviewLight() {
    val prefHelper =
        PreferencesHelper(LocalContext.current.getSharedPreferences("", Context.MODE_PRIVATE))
    TodoListTheme {
        EditScreen(
            viewModel = EditViewModel(
                Repository(RequestManager(prefHelper), DatabaseRepository(testDao)), prefHelper
            ),
            onCancelClick = {},
            toTodosScreen = {},
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EditScreenPreviewNight() {
    val prefHelper =
        PreferencesHelper(LocalContext.current.getSharedPreferences("", Context.MODE_PRIVATE))
    TodoListTheme {
        EditScreen(
            viewModel = EditViewModel(
                Repository(RequestManager(prefHelper), DatabaseRepository(testDao)), prefHelper
            ),
            onCancelClick = {},
            toTodosScreen = {},
        )
    }
}