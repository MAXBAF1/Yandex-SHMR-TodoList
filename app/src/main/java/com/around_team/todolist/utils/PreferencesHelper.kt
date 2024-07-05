package com.around_team.todolist.utils

import android.content.SharedPreferences
import java.util.UUID

class PreferencesHelper(private val preferences: SharedPreferences) {

    companion object {
        private const val KEY_UUID = "uuid"
    }

    fun getUUID(): String {
        var uuid = preferences.getString(KEY_UUID, null)
        if (uuid == null) {
            uuid = UUID
                .randomUUID()
                .toString()

            preferences
                .edit()
                .putString(KEY_UUID, uuid)
                .apply()
        }
        return uuid
    }
}