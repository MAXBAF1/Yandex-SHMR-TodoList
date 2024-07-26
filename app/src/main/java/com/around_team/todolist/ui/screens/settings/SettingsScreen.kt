package com.around_team.todolist.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.around_team.todolist.R
import com.around_team.todolist.ui.common.views.CustomIconButton
import com.around_team.todolist.ui.common.views.CustomTabRow
import com.around_team.todolist.ui.common.views.custom_toolbar.CustomToolbar
import com.around_team.todolist.ui.common.views.custom_toolbar.rememberToolbarScrollBehavior
import com.around_team.todolist.ui.screens.settings.models.SettingsEvent
import com.around_team.todolist.ui.screens.settings.models.Theme
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.utils.SetCompositionTheme

class SettingsScreen(
    private val viewModel: SettingsViewModel,
    private val onBackPressed: () -> Unit,
    private val toAboutScreen: () -> Unit,
) {
    @Composable
    fun Create() {
        val viewState by viewModel
            .getViewState()
            .collectAsStateWithLifecycle()

        SetCompositionTheme(viewState.selectedTheme)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CustomToolbar(
                    navigationIcon = {
                        CustomIconButton(
                            iconId = R.drawable.ic_back,
                            stringResource(R.string.back_semantics),
                            onClick = onBackPressed
                        )
                    },
                    collapsingTitle = stringResource(id = R.string.settings),
                    expandedTitleStyle = JetTodoListTheme.typography.headline,
                    collapsedTitleStyle = JetTodoListTheme.typography.headline,
                    changeTitlePosition = false,
                    scrollBehavior = rememberToolbarScrollBehavior(),
                )
            },
            containerColor = JetTodoListTheme.colors.back.primary,
        ) { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                ThemeRow(
                    selected = viewState.selectedTheme,
                    onChanged = {
                        viewModel.obtainEvent(SettingsEvent.ThemeChanged(it))
                    },
                )
                AboutRow()
            }
        }
    }

    @Composable
    private fun ThemeRow(
        selected: Theme,
        onChanged: (Theme) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val tabList = Theme.entries

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                .semantics(true) { heading() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1F),
                text = stringResource(id = R.string.theme),
                style = JetTodoListTheme.typography.body,
                color = JetTodoListTheme.colors.label.primary
            )
            CustomTabRow(
                modifier = Modifier.weight(1F),
                selectedTab = selected.ordinal,
                tabList = tabList,
                onTabChanged = { onChanged(Theme.getFromOrdinal(it)) },
                highlightSelectedTab = true
            ).Create()
        }
    }

    @Composable
    private fun AboutRow(
        modifier: Modifier = Modifier,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(
                    onClick = toAboutScreen,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple()
                )
                .padding(vertical = 16.dp)
                .semantics {
                    heading()
                    role = Role.Button
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = stringResource(id = R.string.about_app),
                style = JetTodoListTheme.typography.body,
                color = JetTodoListTheme.colors.label.primary
            )
        }
    }
}