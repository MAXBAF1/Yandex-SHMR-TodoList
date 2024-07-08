package com.around_team.todolist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.around_team.todolist.ui.screens.edit.EditScreen
import com.around_team.todolist.ui.screens.edit.EditViewModel
import com.around_team.todolist.ui.screens.todos.TodosScreen
import com.around_team.todolist.ui.screens.todos.TodosViewModel

/**
 * NavGraph manages the navigation within the application using Jetpack Compose Navigation.
 *
 * @param navController The NavHostController responsible for managing navigation within the graph.
 */
class NavGraph(
    private val navController: NavHostController,
) {
    /**
     * Composable function to create and define the navigation graph.
     * This function sets up the NavHost with specified start destination and screens.
     */
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
            composable(
                route = "${Screens.EditScreen.name}?$TO_EDIT_TODO_ID_KEY={$TO_EDIT_TODO_ID_KEY}",
                arguments = listOf(
                    navArgument(TO_EDIT_TODO_ID_KEY) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                )
            ) { CreateEditScreen(editViewModel) }
        }
    }

    /**
     * Composable function to create the Todos screen.
     *
     * @param viewModel The TodosViewModel to provide data and behavior for the Todos screen.
     */
    @Composable
    private fun CreateTodosScreen(viewModel: TodosViewModel) {
        TodosScreen(
            viewModel = viewModel,
            toEditScreen = {
                if (it == null) {
                    navController.navigate(Screens.EditScreen.name)
                } else navController.navigate("${Screens.EditScreen.name}?$TO_EDIT_TODO_ID_KEY=$it")
            },
        ).Create()
    }

    /**
     * Composable function to create the Edit screen.
     *
     * @param viewModel The EditViewModel to provide data and behavior for the Edit screen.
     */
    @Composable
    private fun CreateEditScreen(viewModel: EditViewModel) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val arguments = navBackStackEntry?.arguments
        val editedTodoId = arguments?.getString(TO_EDIT_TODO_ID_KEY, null)

        EditScreen(
            viewModel = viewModel,
            onCancelClick = { navController.popBackStack() },
            toTodosScreen = { navController.navigate(Screens.TodosScreen.name) { popUpTo(0) } },
            editedTodoId = if (editedTodoId.isNullOrBlank()) null else editedTodoId
        ).Create()
    }

    companion object {
        const val TO_EDIT_TODO_ID_KEY = "TO_EDIT_TODO_KEY"
    }
}
