package com.around_team.todolist.di

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import com.around_team.todolist.utils.PreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides dependencies related to SharedPreferences.
 */
@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext app: Context): SharedPreferences {
        return app.getSharedPreferences(KEY, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferencesHelper(sharedPreferences: SharedPreferences): PreferencesHelper {
        return PreferencesHelper(sharedPreferences)
    }

    const val KEY = "my_prefs"
}