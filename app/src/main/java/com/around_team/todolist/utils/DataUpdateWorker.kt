package com.around_team.todolist.utils

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.around_team.todolist.data.network.repositories.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Worker class responsible for updating data in the background.
 *
 * This worker is triggered to refresh all todos asynchronously.
 *
 * @param context The application context provided by WorkManager.
 * @param workerParams Parameters for this worker instance, provided by WorkManager.
 * @param repository The repository interface providing access to data operations.
 */
@HiltWorker
class DataUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: Repository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                repository.refreshAllTodos()
                Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                Result.retry()
            }
        }
    }
}
