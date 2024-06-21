package com.around_team.todolist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.around_team.todolist.ui.screens.edit.EditScreen
import com.around_team.todolist.ui.screens.edit.EditViewModel
import com.around_team.todolist.ui.screens.todos.TodosScreen
import com.around_team.todolist.ui.screens.todos.TodosViewModel

class NavGraph(
    private val navController: NavHostController,
) {
    @Composable
    fun Create() {
        val todosViewModel = hiltViewModel<TodosViewModel>()
        val editViewModel = hiltViewModel<EditViewModel>()

        NavHost(
            navController = navController,
            startDestination = Screens.TodosScreen.name,
            contentAlignment = Alignment.TopStart,
        ) {
            composable(Screens.TodosScreen.name) { CreateTodosScreen(todosViewModel) }
            composable(Screens.EditScreen.name) { CreateEditScreen(editViewModel) }
        }
    }

    @Composable
    private fun CreateTodosScreen(viewModel: TodosViewModel) {
        TodosScreen(
            viewModel = viewModel,
            toEditScreen = { navController.navigate(Screens.EditScreen.name) },
        ).Create()
    }

    @Composable
    private fun CreateEditScreen(viewModel: EditViewModel) {
        EditScreen(
            viewModel = viewModel,
            onCancelClick = { navController.popBackStack() },
            onSaveClick = { },
        ).Create()
    }
}
