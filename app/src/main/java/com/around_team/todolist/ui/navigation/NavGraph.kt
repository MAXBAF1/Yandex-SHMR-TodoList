package com.around_team.todolist.ui.navigation

import android.content.Context
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.around_team.todolist.di.SharedPreferencesModule
import com.around_team.todolist.ui.screens.about.AboutAppScreen
import com.around_team.todolist.ui.screens.edit.EditScreen
import com.around_team.todolist.ui.screens.registration.RegistrationScreen
import com.around_team.todolist.ui.screens.settings.SettingsScreen
import com.around_team.todolist.ui.screens.todos.TodosScreen
import com.around_team.todolist.utils.PreferencesHelper

/**
 * NavGraph manages the navigation within the application using Jetpack Compose Navigation.
 *
 * @param navController The NavHostController responsible for managing navigation within the graph.
 */
class NavGraph(private val navController: NavHostController) {
    /**
     * Composable function to create and define the navigation graph.
     * This function sets up the NavHost with specified start destination and screens.
     */
    @Composable
    fun Create() {
        NavHost(
            navController = navController,
            startDestination = getStartDestination(),
            contentAlignment = Alignment.TopStart,
        ) {
            composable(Screens.RegistrationScreen.name) {
                CreateRegistrationScreen()
            }
            composable(Screens.TodosScreen.name) { CreateTodosScreen() }
            composable(
                route = "${Screens.EditScreen.name}?$TO_EDIT_TODO_ID_KEY={$TO_EDIT_TODO_ID_KEY}",
                arguments = listOf(
                    navArgument(TO_EDIT_TODO_ID_KEY) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                ),
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn()
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut()
                },
                popEnterTransition = {
                    slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn()
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
                },
            ) { CreateEditScreen() }
            composable(Screens.SettingsScreen.name) { CreateSettingsScreen() }
            composable(Screens.AboutAppScreen.name) { CreateAboutAppScreen() }
        }
    }

    @Composable
    private fun CreateAboutAppScreen() {
        AboutAppScreen(
            onBackPressed = { navController.navigate(Screens.SettingsScreen.name) { popUpTo(0) } },
        )
    }

    @Composable
    private fun CreateSettingsScreen() {
        SettingsScreen(
            onBackPressed = { navController.navigate(Screens.TodosScreen.name) { popUpTo(0) } },
            toAboutScreen = { navController.navigate(Screens.AboutAppScreen.name) },
        )
    }


    @Composable
    private fun getStartDestination(): String {
        val context = LocalContext.current
        val sharedPreferences =
            context.getSharedPreferences(SharedPreferencesModule.KEY, Context.MODE_PRIVATE)
        val helper = PreferencesHelper(sharedPreferences)

        return if (helper.getToken() == null) Screens.RegistrationScreen.name else Screens.TodosScreen.name
    }

    @Composable
    private fun CreateRegistrationScreen() {
        RegistrationScreen(
            toNextScreen = { navController.navigate(Screens.TodosScreen.name) { popUpTo(0) } },
        )
    }


    @Composable
    private fun CreateTodosScreen() {
        TodosScreen(
            toEditScreen = {
                if (it == null) {
                    navController.navigate(Screens.EditScreen.name)
                } else navController.navigate("${Screens.EditScreen.name}?$TO_EDIT_TODO_ID_KEY=$it")
            },
            toSettingsScreen = { navController.navigate(Screens.SettingsScreen.name) },
        )
    }

    @Composable
    private fun CreateEditScreen() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val arguments = navBackStackEntry?.arguments
        val editedTodoId = arguments?.getString(TO_EDIT_TODO_ID_KEY, null)

        EditScreen(
            onCancelClick = { navController.popBackStack() },
            toTodosScreen = { navController.navigate(Screens.TodosScreen.name) { popUpTo(0) } },
            editedTodoId = if (editedTodoId.isNullOrBlank()) null else editedTodoId,
        )
    }

    companion object {
        const val TO_EDIT_TODO_ID_KEY = "TO_EDIT_TODO_KEY"
    }
}
