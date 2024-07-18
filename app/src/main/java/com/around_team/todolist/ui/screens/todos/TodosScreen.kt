package com.around_team.todolist.ui.screens.todos

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.around_team.todolist.R
import com.around_team.todolist.data.db.Dao
import com.around_team.todolist.data.db.DatabaseRepository
import com.around_team.todolist.data.db.entities.TodoItemEntity
import com.around_team.todolist.data.network.RequestManager
import com.around_team.todolist.data.network.repositories.Repository
import com.around_team.todolist.ui.common.enums.NetworkConnectionState
import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.ui.common.views.CustomFab
import com.around_team.todolist.ui.common.views.CustomIconButton
import com.around_team.todolist.ui.common.views.CustomSnackbar
import com.around_team.todolist.ui.common.views.MyDivider
import com.around_team.todolist.ui.common.views.custom_toolbar.CustomToolbar
import com.around_team.todolist.ui.common.views.custom_toolbar.CustomToolbarScrollBehavior
import com.around_team.todolist.ui.common.views.custom_toolbar.rememberToolbarScrollBehavior
import com.around_team.todolist.ui.screens.todos.models.TodosEvent
import com.around_team.todolist.ui.screens.todos.views.CreateNewCard
import com.around_team.todolist.ui.screens.todos.views.TodoCard
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.ui.theme.TodoListTheme
import com.around_team.todolist.utils.PreferencesHelper
import com.around_team.todolist.utils.observeConnectivityAsFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Screen component displaying a list of todo items.
 *
 * @param viewModel View model managing the state and logic for todos.
 * @param toEditScreen Callback function to navigate to the edit screen with optional todo item ID.
 */
class TodosScreen(
    private val viewModel: TodosViewModel,
    private val toEditScreen: (id: String?) -> Unit,
    private val toSettingsScreen: () -> Unit,
) {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Create() {
        val viewState by viewModel
            .getViewState()
            .collectAsStateWithLifecycle()
        val scrollBehavior = rememberToolbarScrollBehavior()

        LaunchedEffect(Unit) { viewModel.obtainEvent(TodosEvent.StartCollecting) }

        val snackbarHostState = remember { SnackbarHostState() }
        MessagesLogic(viewState.messageId, snackbarHostState)

        val pullState = rememberPullToRefreshState()
        PullToRefreshLogic(pullState, viewState.refreshing, scrollBehavior)

        NetworkLogic(viewState.connectionState)

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .let {
                    if (scrollBehavior.state.collapsedFraction == 0f) {
                        it.nestedScroll(pullState.nestedScrollConnection)
                    } else it
                },
            topBar = {
                CustomToolbar(collapsingTitle = stringResource(id = R.string.title),
                    scrollBehavior = scrollBehavior,
                    actions = {
                        CustomIconButton(
                            iconId = R.drawable.ic_settings, onClick = toSettingsScreen
                        )
                    })
            },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                CustomFab(
                    modifier = Modifier.padding(bottom = 20.dp),
                    onClick = { toEditScreen(null) },
                )
            },
            snackbarHost = { CustomSnackbar(hostState = snackbarHostState) },
            containerColor = JetTodoListTheme.colors.back.primary,
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                TodoList(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxSize(),
                    completedTodosShowed = viewState.completedShowed,
                    todos = viewState.todos,
                    completeCnt = viewState.completeCnt,
                    onShowClick = { viewModel.obtainEvent(TodosEvent.ClickShowCompletedTodos) },
                    onCompleteClick = { viewModel.obtainEvent(TodosEvent.CompleteTodo(it)) },
                    onDelete = { viewModel.obtainEvent(TodosEvent.DeleteTodo(it)) },
                    onTodoClick = { toEditScreen(it) },
                )
                PullToRefreshContainer(
                    state = pullState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    containerColor = JetTodoListTheme.colors.back.primary,
                    contentColor = JetTodoListTheme.colors.label.primary
                )
            }
        }
    }

    @Composable
    private fun NetworkLogic(connectionViewState: NetworkConnectionState) {
        val networkState = LocalContext.current
            .observeConnectivityAsFlow()
            .collectAsState(NetworkConnectionState.Available)
        LaunchedEffect(networkState.value) {
            if (networkState.value != connectionViewState) {
                viewModel.obtainEvent(TodosEvent.HandleNetworkState(networkState.value))
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun PullToRefreshLogic(
        pullState: PullToRefreshState,
        refreshing: Boolean,
        scrollBehavior: CustomToolbarScrollBehavior,
    ) {
        LaunchedEffect(pullState.isRefreshing) {
            if (pullState.isRefreshing) viewModel.obtainEvent(TodosEvent.RefreshTodos)
        }

        LaunchedEffect(key1 = refreshing) {
            if (!refreshing) pullState.endRefresh()
        }

        if (scrollBehavior.state.collapsedFraction != 0f) pullState.endRefresh()
    }

    @Composable
    private fun MessagesLogic(
        messageId: Int?,
        snackbarHostState: SnackbarHostState,
    ) {
        if (messageId == null) return
        val notActionMessages = listOf(R.string.network_unavailable, R.string.success_sync)
        val messageStr = stringResource(messageId)
        var countdownTime = 5
        val actionStr = stringResource(
            when (messageId) {
                R.string.todo_deleted -> R.string.cancel
                else -> R.string.repeat
            }
        )

        val context = LocalContext.current

        LaunchedEffect(key1 = messageStr) {
            if (notActionMessages.contains(messageId)) {
                Toast
                    .makeText(context, messageStr, Toast.LENGTH_LONG)
                    .show()
                return@LaunchedEffect
            } else if (messageId == R.string.todo_deleted) {
                launch(Dispatchers.IO) {
                    while (countdownTime > 0) {
                        delay(1000)
                        countdownTime--
                    }
                    snackbarHostState.currentSnackbarData?.dismiss()
                }
            }
            val result = snackbarHostState.showSnackbar(messageStr, actionStr)
            viewModel.obtainEvent(TodosEvent.HandleSnackbarResult(result))
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun TodoList(
        completedTodosShowed: Boolean,
        todos: List<TodoItem>,
        completeCnt: Int,
        onShowClick: () -> Unit,
        onCompleteClick: (id: String) -> Unit,
        onDelete: (id: String) -> Unit,
        onTodoClick: (id: String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
        ) {
            item {
                CompleteRow(
                    showed = completedTodosShowed,
                    completeCnt = completeCnt,
                    onShowClick = onShowClick,
                    modifier = Modifier
                        .padding(start = 32.dp, top = 8.dp, end = 32.dp, bottom = 12.dp)
                        .background(JetTodoListTheme.colors.back.primary),
                )
            }
            itemsIndexed(items = todos, key = { _, todo -> todo.id }) { i, todo ->
                TodoRow(
                    modifier = if (i == 0) {
                        Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    } else Modifier
                        .animateItemPlacement()
                        .background(JetTodoListTheme.colors.back.secondary),
                    todo = todo,
                    onClick = { onTodoClick(todo.id) },
                    onCompleteClick = { onCompleteClick(todo.id) },
                    onDelete = { onDelete(todo.id) },
                )
            }
            item {
                CreateNewCard(
                    modifier = Modifier
                        .padding(bottom = 100.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = if (todos.isEmpty()) 16.dp else 0.dp,
                                topEnd = if (todos.isEmpty()) 16.dp else 0.dp,
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            )
                        ),
                    onClick = { toEditScreen(null) },
                )
            }
        }
    }

    @Composable
    private fun CompleteRow(
        showed: Boolean,
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
                text = stringResource(R.string.complete, completeCnt),
                style = JetTodoListTheme.typography.subhead,
                color = JetTodoListTheme.colors.label.tertiary
            )
            Text(
                modifier = Modifier.clickable(
                    onClick = onShowClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ),
                text = stringResource(id = if (showed) R.string.hide else R.string.show),
                style = JetTodoListTheme.typography.subhead,
                fontWeight = FontWeight.Bold,
                color = JetTodoListTheme.colors.colors.blue
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TodoRow(
        todo: TodoItem,
        onClick: () -> Unit,
        onCompleteClick: () -> Unit,
        onDelete: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val dismissState = rememberSwipeToDismissBoxState(
            confirmValueChange = {
                when (it) {
                    SwipeToDismissBoxValue.StartToEnd -> onCompleteClick()
                    SwipeToDismissBoxValue.EndToStart -> onDelete()
                    SwipeToDismissBoxValue.Settled -> {}
                }

                return@rememberSwipeToDismissBoxState false
            },
            positionalThreshold = { it * 0.25F },
        )
        SwipeToDismissBox(
            state = dismissState,
            modifier = modifier,
            backgroundContent = {
                SwipeBackgroundContent(dismissState)
            },
            content = {
                Column {
                    TodoCard(todo = todo, onClick = onClick, onCompleteClick = onCompleteClick)
                    MyDivider()
                }
            },
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SwipeBackgroundContent(dismissState: SwipeToDismissBoxState) {
        when (dismissState.dismissDirection) {
            SwipeToDismissBoxValue.StartToEnd -> {
                Box(
                    modifier = Modifier
                        .background(JetTodoListTheme.colors.colors.green)
                        .fillMaxSize()
                        .padding(start = 20.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    SwipeIcon(R.drawable.ic_complete, R.string.complete_icon)
                }
            }

            SwipeToDismissBoxValue.EndToStart -> {
                Box(
                    modifier = Modifier
                        .background(JetTodoListTheme.colors.colors.red)
                        .fillMaxSize()
                        .padding(end = 20.dp),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    SwipeIcon(R.drawable.ic_delete, R.string.delete_icon)
                }
            }

            SwipeToDismissBoxValue.Settled -> {}
        }
    }

    @Composable
    private fun SwipeIcon(iconId: Int, descriptionId: Int) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = iconId),
            contentDescription = stringResource(descriptionId),
            tint = JetTodoListTheme.colors.colors.white
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun TodosScreenPreviewLight() {
    val prefHelper =
        PreferencesHelper(LocalContext.current.getSharedPreferences("", Context.MODE_PRIVATE))
    TodoListTheme {
        TodosScreen(
            viewModel = TodosViewModel(
                Repository(RequestManager(prefHelper), DatabaseRepository(testDao)), prefHelper
            ),
            toEditScreen = {},
            toSettingsScreen = {},
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TodosScreenPreviewNight() {
    val prefHelper =
        PreferencesHelper(LocalContext.current.getSharedPreferences("", Context.MODE_PRIVATE))
    TodoListTheme {
        TodosScreen(
            viewModel = TodosViewModel(
                Repository(RequestManager(prefHelper), DatabaseRepository(testDao)), prefHelper
            ),
            toEditScreen = {},
            toSettingsScreen = {},
        )
    }
}

val testDao = object : Dao {
    override suspend fun insertTodo(todo: TodoItemEntity) {}
    override suspend fun insertTodos(todos: List<TodoItemEntity>) {}
    override suspend fun getAllTodos(): List<TodoItemEntity> = emptyList()
    override suspend fun deleteTodoById(todoId: String) {}
    override suspend fun deleteAllTodos() {}
}