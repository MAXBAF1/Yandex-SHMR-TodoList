package com.around_team.todolist.ui.common.views.custom_toolbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.utils.background
import com.around_team.todolist.utils.lerp
import kotlin.math.roundToInt

@Composable
fun CustomToolbar(
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    collapsingTitle: String? = null,
    expandedTitleStyle: TextStyle = JetTodoListTheme.typography.largeTitle,
    collapsedTitleStyle: TextStyle = JetTodoListTheme.typography.collapsedLargeTitle,
    changeTitlePosition: Boolean = true,
    scrollBehavior: CustomToolbarScrollBehavior? = null,
) {
    val collapsedFraction = scrollBehavior?.state?.collapsedFraction ?: 1f

    val fullyCollapsedTitleScale = when {
        collapsingTitle != null -> collapsedTitleStyle.lineHeight.value / expandedTitleStyle.lineHeight.value
        else -> 1f
    }

    val collapsingTitleScale = lerp(1f, fullyCollapsedTitleScale, collapsedFraction)

    val showBorder = when {
        scrollBehavior == null -> false
        scrollBehavior.state.contentOffset <= 0 && collapsedFraction == 1f -> true
        else -> false
    }

    val borderState = if (showBorder) 0.5.dp else 0.dp

    val bgColor = animateColorAsState(
        targetValue = lerp(
            JetTodoListTheme.colors.back.primary,
            JetTodoListTheme.colors.support.navbarBlur,
            collapsedFraction
        ), label = ""
    )

    Surface(
        modifier = Modifier.background { bgColor.value },
        color = Color.Transparent,
    ) {
        Column {
            Layout(
                content = {
                    if (collapsingTitle != null) {
                        Text(
                            modifier = Modifier
                                .layoutId(ExpandedTitleId)
                                .wrapContentHeight(align = Alignment.Top)
                                .graphicsLayer(
                                    scaleX = collapsingTitleScale,
                                    scaleY = collapsingTitleScale,
                                    transformOrigin = TransformOrigin(0f, 0f)
                                ),
                            text = collapsingTitle,
                            style = expandedTitleStyle,
                            color = JetTodoListTheme.colors.label.primary
                        )
                        Text(
                            modifier = Modifier
                                .layoutId(CollapsedTitleId)
                                .graphicsLayer(
                                    scaleX = collapsingTitleScale,
                                    scaleY = collapsingTitleScale,
                                    transformOrigin = TransformOrigin(0f, 0f)
                                )
                                .wrapContentHeight(align = Alignment.Bottom),
                            text = collapsingTitle,
                            style = collapsedTitleStyle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = JetTodoListTheme.colors.label.primary
                        )
                    }

                    if (navigationIcon != null) {
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .layoutId(NavigationIconId)
                        ) { navigationIcon() }
                    }

                    if (actions != null) {
                        Row(
                            modifier = Modifier
                                .wrapContentSize()
                                .layoutId(ActionsId)
                        ) { actions() }
                    }
                }, modifier = modifier
                    .then(Modifier.heightIn(min = MinCollapsedHeight))
                    .padding(
                        WindowInsets.systemBars
                            .only(WindowInsetsSides.Top)
                            .asPaddingValues()
                    )
            ) { measurables, constraints ->
                val horizontalPaddingPx = HorizontalPadding.toPx()


                // Measuring widgets inside toolbar:

                val navigationIconPlaceable =
                    measurables.firstOrNull { it.layoutId == NavigationIconId }
                        ?.measure(constraints.copy(minWidth = 0))

                val actionsPlaceable = measurables.firstOrNull { it.layoutId == ActionsId }
                    ?.measure(constraints.copy(minWidth = 0))

                val expandedTitlePlaceable =
                    measurables.firstOrNull { it.layoutId == ExpandedTitleId }?.measure(
                        constraints.copy(
                            maxWidth = (constraints.maxWidth - 2 * horizontalPaddingPx).roundToInt(),
                            minWidth = 0,
                            minHeight = 0
                        )
                    )

                val navigationIconOffset = when (navigationIconPlaceable) {
                    null -> horizontalPaddingPx
                    else -> navigationIconPlaceable.width + horizontalPaddingPx * 2
                }

                val actionsOffset = when (actionsPlaceable) {
                    null -> horizontalPaddingPx
                    else -> actionsPlaceable.width + horizontalPaddingPx * 2
                }

                val collapsedTitleMaxWidthPx =
                    (constraints.maxWidth - navigationIconOffset - actionsOffset) / fullyCollapsedTitleScale

                val collapsedTitlePlaceable =
                    measurables.firstOrNull { it.layoutId == CollapsedTitleId }?.measure(
                        constraints.copy(
                            maxWidth = collapsedTitleMaxWidthPx.roundToInt(),
                            minWidth = 0,
                            minHeight = 0
                        )
                    )


                val collapsedHeightPx = MinCollapsedHeight.toPx()

                var layoutHeightPx = collapsedHeightPx


                // Calculating coordinates of widgets inside toolbar:

                // Current coordinates of navigation icon
                val navigationIconX = horizontalPaddingPx.roundToInt()
                val navigationIconY =
                    ((collapsedHeightPx - (navigationIconPlaceable?.height ?: 0)) / 2).roundToInt()

                // Current coordinates of actions
                val actionsX = (constraints.maxWidth - (actionsPlaceable?.width
                    ?: 0) - horizontalPaddingPx).roundToInt()
                val actionsY =
                    ((collapsedHeightPx - (actionsPlaceable?.height ?: 0)) / 2).roundToInt()

                // Current coordinates of title
                var collapsingTitleY = 0
                var collapsingTitleX = 0

                if (expandedTitlePlaceable != null && collapsedTitlePlaceable != null) {
                    // Measuring toolbar collapsing distance
                    val heightOffsetLimitPx = expandedTitlePlaceable.height.toFloat()
                    scrollBehavior?.state?.heightOffsetLimit = -heightOffsetLimitPx

                    // Toolbar height at fully expanded state
                    val fullyExpandedHeightPx = MinCollapsedHeight.toPx() + heightOffsetLimitPx

                    // Coordinates of fully expanded title
                    val fullyExpandedTitleX = horizontalPaddingPx
                    val fullyExpandedTitleY = fullyExpandedHeightPx - expandedTitlePlaceable.height

                    // Coordinates of fully collapsed title
                    val fullyCollapsedTitleX =
                        constraints.maxWidth / 2F - collapsedTitlePlaceable.width
                    val fullyCollapsedTitleY =
                        collapsedHeightPx / 2 - CollapsedTitleLineHeight.toPx().roundToInt() / 2

                    // Current height of toolbar
                    layoutHeightPx =
                        lerp(fullyExpandedHeightPx, collapsedHeightPx, collapsedFraction)

                    // Current coordinates of collapsing title
                    collapsingTitleX = lerp(
                        fullyExpandedTitleX,
                        fullyCollapsedTitleX,
                        if (changeTitlePosition) collapsedFraction else 1F
                    ).roundToInt()
                    collapsingTitleY = lerp(
                        fullyExpandedTitleY,
                        fullyCollapsedTitleY,
                        if (changeTitlePosition) collapsedFraction else 1F
                    ).roundToInt()
                } else {
                    scrollBehavior?.state?.heightOffsetLimit = -1f
                }

                val toolbarHeightPx = layoutHeightPx.roundToInt()


                // Placing toolbar widgets:

                layout(constraints.maxWidth, toolbarHeightPx) {
                    navigationIconPlaceable?.placeRelative(x = navigationIconX, y = navigationIconY)
                    actionsPlaceable?.placeRelative(x = actionsX, y = actionsY)
                    if (expandedTitlePlaceable?.width == collapsedTitlePlaceable?.width) {
                        expandedTitlePlaceable?.placeRelative(
                            x = collapsingTitleX, y = collapsingTitleY
                        )
                    } else {
                        expandedTitlePlaceable?.placeRelativeWithLayer(
                            x = collapsingTitleX,
                            y = collapsingTitleY,
                            layerBlock = { alpha = 1 - collapsedFraction },
                        )
                        collapsedTitlePlaceable?.placeRelativeWithLayer(
                            x = collapsingTitleX,
                            y = collapsingTitleY,
                            layerBlock = { alpha = collapsedFraction },
                        )
                    }
                }
            }
            if (showBorder) {
                Divider(
                    thickness = borderState, color = JetTodoListTheme.colors.support.separator
                )
            }
        }
    }
}

private val HorizontalPadding = 16.dp
private val CollapsedTitleLineHeight = 20.sp
private val MinCollapsedHeight = 46.dp

private const val ExpandedTitleId = "expandedTitle"
private const val CollapsedTitleId = "collapsedTitle"
private const val NavigationIconId = "navigationIcon"
private const val ActionsId = "actions"