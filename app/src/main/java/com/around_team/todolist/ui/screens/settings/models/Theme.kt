package com.around_team.todolist.ui.screens.settings.models

import com.around_team.todolist.R
import com.around_team.todolist.ui.common.enums.ITabs

enum class Theme(
    override val text: Int? = null,
    override val iconId: Int? = null,
    override val descriptionId: Int
) : ITabs {
    Sun(iconId = R.drawable.ic_sun, descriptionId = R.string.light_semantics),
    Auto(text = R.string.auto, descriptionId = R.string.system_semantics),
    Moon(iconId = R.drawable.ic_moon, descriptionId = R.string.night_semantics);

    companion object {
        fun getFromOrdinal(ordinal: Int): Theme {
            return when (ordinal) {
                0 -> Sun
                1 -> Auto
                2 -> Moon
                else -> throw IllegalArgumentException(ordinal.toString())
            }
        }
    }
}
