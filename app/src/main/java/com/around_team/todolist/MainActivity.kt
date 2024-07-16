package com.around_team.todolist

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.around_team.todolist.di.SharedPreferencesModule
import com.around_team.todolist.ui.navigation.NavGraph
import com.around_team.todolist.ui.screens.settings.models.ThemeTabs
import com.around_team.todolist.ui.theme.TodoListTheme
import com.around_team.todolist.utils.DataUpdateWorker
import com.around_team.todolist.ui.theme.LocalSettingsEventBus
import com.around_team.todolist.ui.theme.SettingsEventBus
import com.around_team.todolist.utils.PreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


/**
 * MainActivity for the TodoList application.
 *
 * This activity sets up the main components of the application,
 * including navigation and periodic data updates.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *     this Bundle contains the data it most recently supplied in [onSaveInstanceState].
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val settingsEventBus = remember { SettingsEventBus() }
            val navController = rememberNavController()

            TodoListTheme(darkTheme = isDarkThemeSaved()) {
                CompositionLocalProvider(LocalSettingsEventBus provides settingsEventBus) {
                    NavGraph(navController = navController).Create()
                }
            }
        }

        setupPeriodicWork()
    }

    @Composable
    private fun isDarkThemeSaved(): Boolean {
        val prefHelper = PreferencesHelper(
            LocalContext.current.getSharedPreferences(
                SharedPreferencesModule.KEY, Context.MODE_PRIVATE
            )
        )
        return when (prefHelper.getSelectedTheme()) {
            ThemeTabs.Sun -> false
            ThemeTabs.Auto -> isSystemInDarkTheme()
            ThemeTabs.Moon -> true
            null -> isSystemInDarkTheme()
        }
    }

    /**
     * Sets up periodic background work to refresh data at regular intervals.
     */
    private fun setupPeriodicWork() {
        val workRequest = PeriodicWorkRequestBuilder<DataUpdateWorker>(8, TimeUnit.HOURS).build()

        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                "DataUpdateWork", ExistingPeriodicWorkPolicy.KEEP, workRequest
            )
    }
}
