package com.around_team.todolist.utils

import android.content.SharedPreferences
import java.util.UUID

/**
 * Helper class for managing preferences related to UUID.
 *
 * @property preferences The [SharedPreferences] instance used to store and retrieve preferences.
 */
class PreferencesHelper(private val preferences: SharedPreferences) {

    companion object {
        private const val KEY_UUID = "uuid"
        private const val KEY_TOKEN = "KEY_TOKEN"
    }

    fun getUUID(): String {
        var uuid = preferences.getString(KEY_UUID, null)
        if (uuid == null) {
            uuid = UUID.randomUUID().toString()
            preferences.edit()
                .putString(KEY_UUID, uuid)
                .apply()
        }
        return uuid
    }

    fun saveToken(token: String) {
        preferences.edit()
            .putString(KEY_TOKEN, token)
            .apply()
    }

    fun getToken(): String? {
        return preferences.getString(KEY_TOKEN, null)
    }
}
