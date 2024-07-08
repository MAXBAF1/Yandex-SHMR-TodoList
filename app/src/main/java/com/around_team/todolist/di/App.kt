package com.around_team.todolist.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom [Application] class annotated with [HiltAndroidApp] to enable Hilt for dependency injection.
 */
@HiltAndroidApp
class App : Application()