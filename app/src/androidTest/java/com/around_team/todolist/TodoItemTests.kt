package com.around_team.todolist

import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.filters.LargeTest
import com.around_team.todolist.data.network.NetworkConfig
import com.around_team.todolist.ui.navigation.NavGraph
import com.around_team.todolist.ui.theme.TodoListTheme
import com.around_team.todolist.utils.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.HttpMethod
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@LargeTest
class TodoItemTests {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private fun stringResource(@StringRes id: Int) = composeTestRule.activity.getString(id)

    @Inject
    lateinit var mockEngine: MockEngine

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun addTodoItem() = runBlocking {
        composeTestRule.setContent {
            TodoListTheme {
                NavGraph(navController = rememberNavController(), needAuthenticate = false).Create()
            }
        }

        composeTestRule
            .onNodeWithTag(TestTags.FAB_TAG)
            .performClick()

        val text = "this is todo text"
        composeTestRule
            .onNodeWithTag(TestTags.TEXT_FIELD_TAG)
            .performTextInput(text)
        composeTestRule
            .onNodeWithText(stringResource(R.string.save))
            .performClick()

        composeTestRule
            .onNodeWithTag(TestTags.TEXT_FIELD_TAG)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithText(text)
            .assertExists()

        val myRequest = mockEngine.requestHistory.filter {
                val isPost = it.method == HttpMethod.Post
                val isRightAddress = it.url.toString() == NetworkConfig.LIST_ADDRESS.toString()
                val isContainsText = it.body
                    .toByteArray()
                    .decodeToString()
                    .contains(text)

                isPost && isRightAddress && isContainsText
            }

        assert(myRequest.isNotEmpty())
    }

    @Test
    fun deleteTodoItem() = runBlocking {
        composeTestRule.setContent {
            TodoListTheme {
                NavGraph(navController = rememberNavController(), needAuthenticate = false).Create()
            }
        }

        composeTestRule
            .onNodeWithTag(TestTags.FAB_TAG)
            .performClick()

        val text = "this is todo text"
        composeTestRule
            .onNodeWithTag(TestTags.TEXT_FIELD_TAG)
            .performTextInput(text)
        composeTestRule
            .onNodeWithText(stringResource(R.string.save))
            .performClick()

        composeTestRule
            .onNodeWithText(text)
            .assertExists()
        composeTestRule
            .onNodeWithText(text)
            .performClick()
        composeTestRule
            .onNodeWithText(stringResource(R.string.delete))
            .performClick()

        composeTestRule
            .onNodeWithText(text)
            .assertDoesNotExist()

        val myRequest = mockEngine.requestHistory.filter {
                it.method == HttpMethod.Delete
            }

        assert(myRequest.isNotEmpty())
    }
}