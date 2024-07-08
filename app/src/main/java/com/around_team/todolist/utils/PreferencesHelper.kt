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
    }

    /**
     * Retrieves the UUID stored in preferences. If no UUID is found, generates a new UUID,
     * stores it in preferences, and returns it.
     *
     * @return The UUID string.
     */
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
}
