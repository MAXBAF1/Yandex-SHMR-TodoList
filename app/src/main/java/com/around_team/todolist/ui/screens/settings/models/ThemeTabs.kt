package com.around_team.todolist.ui.screens.settings.models

import com.around_team.todolist.R
import com.around_team.todolist.ui.common.enums.ITabs

enum class ThemeTabs(override val text: Int? = null, override val iconId: Int? = null) : ITabs {
    Sun(iconId = R.drawable.ic_sun), Auto(text = R.string.auto), Moon(iconId = R.drawable.ic_moon);

    companion object {
        fun getFromOrdinal(ordinal: Int): ThemeTabs {
            return when (ordinal) {
                0 -> Sun
                1 -> Auto
                2 -> Moon
                else -> throw IllegalArgumentException(ordinal.toString())
            }
        }
    }
}
