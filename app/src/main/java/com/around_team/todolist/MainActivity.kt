package com.around_team.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.around_team.todolist.ui.navigation.NavGraph
import com.around_team.todolist.ui.theme.TodoListTheme
import com.around_team.todolist.utils.DataUpdateWorker
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
            TodoListTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController).Create()
            }
        }

        setupPeriodicWork()
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
