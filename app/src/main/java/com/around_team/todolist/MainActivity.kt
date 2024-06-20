package com.around_team.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.around_team.todolist.ui.common.views.CustomFab
import com.around_team.todolist.ui.common.views.custom_toolbar.CustomToolbar
import com.around_team.todolist.ui.common.views.custom_toolbar.rememberToolbarScrollBehavior
import com.around_team.todolist.ui.navigation.NavGraph
import com.around_team.todolist.ui.navigation.Screens
import com.around_team.todolist.ui.theme.TodoListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListTheme {
                val scrollBehavior = rememberToolbarScrollBehavior()
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val showBars = currentRoute == Screens.TodosScreen.name

                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        AnimatedVisibility(
                            visible = showBars,
                            enter = slideInVertically() + expandVertically(expandFrom = Alignment.Top) + fadeIn(
                                initialAlpha = 0.3f
                            ),
                            exit = slideOutVertically() + shrinkVertically() + fadeOut()
                        ) {
                            CustomToolbar(
                                collapsingTitle = stringResource(id = R.string.title),
                                scrollBehavior = scrollBehavior
                            )
                        }
                    },
                    floatingActionButtonPosition = FabPosition.Center,
                    floatingActionButton = {
                        AnimatedVisibility(
                            visible = showBars,
                            enter = slideInVertically { 40 } + expandVertically() + fadeIn(
                                initialAlpha = 0.3f
                            ),
                            exit = slideOutVertically { 40 } + shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
                        ) {
                            CustomFab(
                                modifier = Modifier.padding(bottom = 20.dp),
                                onClick = { navController.navigate(Screens.EditScreen.name) },
                            )
                        }
                    },
                ) { contentPadding ->
                    NavGraph(navController = navController, innerPaddings = contentPadding).Create()
                }
            }
        }
    }
}