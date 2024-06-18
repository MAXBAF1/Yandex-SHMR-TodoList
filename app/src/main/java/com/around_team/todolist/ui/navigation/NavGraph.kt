package com.around_team.todolist.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.around_team.todolist.ui.screens.todos.TodosScreen
import com.around_team.todolist.ui.screens.todos.TodosViewModel

class NavGraph(
    private val navController: NavHostController,
    private val innerPaddings: PaddingValues,
) {
    @Composable
    fun Create() {
        val todosViewModel = hiltViewModel<TodosViewModel>()
        NavHost(
            navController = navController,
            startDestination = Screens.MainScreen.name,
            modifier = Modifier.padding(top = innerPaddings.calculateTopPadding()),
            contentAlignment = Alignment.TopStart
        ) {
            composable(Screens.MainScreen.name) { CreateTodosScreen(todosViewModel) }
        }
    }

    @Composable
    private fun CreateTodosScreen(viewModel: TodosViewModel) {
        TodosScreen(viewModel).Create()
    }
}
