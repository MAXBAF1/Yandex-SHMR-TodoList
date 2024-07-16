package com.around_team.todolist.ui.screens.edit.views

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.around_team.todolist.R
import com.around_team.todolist.ui.common.enums.ITabs
import com.around_team.todolist.ui.common.enums.TodoImportance
import com.around_team.todolist.ui.common.enums.getIconColor
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.ui.theme.TodoListTheme

/**
 * A custom composable that displays a tab row for selecting different levels of todo importance.
 * Each tab represents a level of importance defined by [tabList].
 *
 * @param selectedTab The index of the currently selected tab.
 * @param tabList An array of [TodoImportance] values representing different levels of importance.
 * @param onTabChanged Lambda that will be called when a tab is selected. It provides the index of the selected tab.
 * @param modifier Optional modifier for styling or positioning the tab row.
 */
class CustomTabRow(
    private val selectedTab: Int,
    private val tabList: List<ITabs>,
    private val onTabChanged: (Int) -> Unit,
    private val modifier: Modifier = Modifier,
    private val highlightSelectedTab: Boolean = false,
) {

    @Composable
    fun Create() {
        val indicator = @Composable { tabPositions: List<TabPosition> ->
            CustomIndicator(tabPositions, selectedTab)
        }
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(9.dp))
                .background(JetTodoListTheme.colors.support.overlay)
        ) {
            TabRow(
                modifier = Modifier.padding(2.dp),
                containerColor = Color.Transparent,
                selectedTabIndex = selectedTab,
                indicator = indicator,
                divider = {},
            ) {
                tabList.forEachIndexed { index, tab ->
                    val tabsTextColor = if (highlightSelectedTab && index == selectedTab) {
                        JetTodoListTheme.colors.label.primary
                    } else JetTodoListTheme.colors.colors.gray

                    CustomTab(
                        tab = tab, onClick = { onTabChanged(index) }, color = tabsTextColor
                    )
                }
            }
        }
    }

    @Composable
    private fun CustomIndicator(tabPositions: List<TabPosition>, selectedTab: Int) {
        val transition = updateTransition(selectedTab, label = "")
        val indicatorStart by transition.animateDp(
            transitionSpec = {
                if (initialState < targetState) {
                    spring(dampingRatio = 1f, stiffness = 250f)
                } else {
                    spring(dampingRatio = 1f, stiffness = 1000f)
                }
            }, label = ""
        ) { tabPositions[it].left }

        val indicatorEnd by transition.animateDp(
            transitionSpec = {
                if (initialState < targetState) {
                    spring(dampingRatio = 1f, stiffness = 1000f)
                } else {
                    spring(dampingRatio = 1f, stiffness = 250f)
                }
            }, label = ""
        ) { tabPositions[it].right }

        Box(
            Modifier
                .offset(x = indicatorStart)
                .wrapContentSize(align = Alignment.BottomStart)
                .width(indicatorEnd - indicatorStart)
                .fillMaxSize()
                .background(color = JetTodoListTheme.colors.back.elevated, RoundedCornerShape(7.dp))

        )
    }

    @Composable
    private fun CustomTab(
        tab: ITabs,
        onClick: () -> Unit,
        color: Color,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(7.dp))
                .size(48.dp, 32.dp)
                .zIndex(1f)
                .clickable(
                    onClick = onClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (tab.iconId != null) {
                Icon(
                    painter = painterResource(id = tab.iconId ?: R.drawable.ic_error),
                    contentDescription = stringResource(id = R.string.priority_icon),
                    tint = if (tab is TodoImportance) tab.getIconColor() else color
                )
            } else if (tab.text != null) {
                Text(
                    text = stringResource(id = tab.text ?: R.string.error),
                    style = JetTodoListTheme.typography.body,
                    color = if (highlightSelectedTab) color else JetTodoListTheme.colors.label.primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun CustomTabRowPreview() {
    TodoListTheme {
        Box(modifier = Modifier.background(JetTodoListTheme.colors.back.primary)) {
            CustomTabRow(
                selectedTab = 1,
                tabList = TodoImportance.entries,
                onTabChanged = { },
            ).Create()
        }
    }
}