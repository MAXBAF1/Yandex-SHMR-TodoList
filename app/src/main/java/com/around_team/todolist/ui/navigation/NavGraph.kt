package com.around_team.todolist.ui.navigation

import android.net.Uri
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
import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.ui.screens.edit.EditScreen
import com.around_team.todolist.ui.screens.edit.EditViewModel
import com.around_team.todolist.ui.screens.todos.TodosScreen
import com.around_team.todolist.ui.screens.todos.TodosViewModel
import com.google.gson.Gson

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
            composable(
                route = "${Screens.TodosScreen.name}?$TO_TODOS_TODO_KEY={$TO_TODOS_TODO_KEY}&$DELETE_KEY={$DELETE_KEY}",
                arguments = listOf(
                    navArgument(TO_TODOS_TODO_KEY) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument(DELETE_KEY) {
                        type = NavType.BoolType
                        defaultValue = false
                    },
                ),
            ) { CreateTodosScreen(todosViewModel) }
            composable(
                route = "${Screens.EditScreen.name}?$TO_EDIT_TODO_KEY={$TO_EDIT_TODO_KEY}",
                arguments = listOf(
                    navArgument(TO_EDIT_TODO_KEY) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                )
            ) { CreateEditScreen(editViewModel) }
        }
    }

    @Composable
    private fun CreateTodosScreen(viewModel: TodosViewModel) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val arguments = navBackStackEntry?.arguments
        val newTodo =
            Gson().fromJson(arguments?.getString(TO_TODOS_TODO_KEY, null), TodoItem::class.java)
        val deleteTodo = arguments?.getBoolean(DELETE_KEY, false) ?: false

        TodosScreen(
            viewModel = viewModel,
            toEditScreen = { navigateWithTodo(Screens.EditScreen.name, TO_EDIT_TODO_KEY, it) },
            newTodo = newTodo,
            deleteTodo = deleteTodo
        ).Create()
    }

    @Composable
    private fun CreateEditScreen(viewModel: EditViewModel) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val arguments = navBackStackEntry?.arguments
        val editedTodo =
            Gson().fromJson(arguments?.getString(TO_EDIT_TODO_KEY, null), TodoItem::class.java)


        EditScreen(
            viewModel = viewModel,
            onCancelClick = { navController.popBackStack() },
            onSaveClick = { todo, delete ->
                navigateWithTodo(Screens.TodosScreen.name, TO_TODOS_TODO_KEY, todo, delete)
            },
            editedTodo = editedTodo
        ).Create()
    }

    private fun navigateWithTodo(
        route: String,
        key: String,
        todo: TodoItem?,
        delete: Boolean = false,
    ) {
        val jsonTodo = Gson().toJson(todo)
        val encodedTodo = Uri.encode(jsonTodo)
        if (todo == null) {
            navController.navigate(route)
        } else navController.navigate("$route?$key=$encodedTodo&$DELETE_KEY=$delete")
    }

    companion object {
        const val TO_TODOS_TODO_KEY = "TO_TODOS_TODO_KEY"
        const val TO_EDIT_TODO_KEY = "TO_EDIT_TODO_KEY"
        const val DELETE_KEY = "DELETE_KEY"
    }
}
