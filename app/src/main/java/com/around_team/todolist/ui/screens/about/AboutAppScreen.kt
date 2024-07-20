package com.around_team.todolist.ui.screens.about

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.yandex.div.DivDataTag
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.view2.Div2View
import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div.json.ParsingErrorLogger
import com.yandex.div.picasso.PicassoDivImageLoader
import com.yandex.div2.DivData
import org.json.JSONObject

class AboutAppScreen(private val onBackPressed: () -> Unit) {
    private val noopReset: (View) -> Unit = {}

    @Composable
    fun Create() {
        val context = LocalContext.current

        val inputStream = context.assets.open("aboutScreen.json")
        val json = inputStream
            .bufferedReader()
            .use { it.readText() }
        val data = JSONObject(json).asDiv2DataWithTemplates()

        DivView(data = data, tag = DivDataTag("div2"))
    }

    @Composable
    fun DivView(
        data: DivData,
        tag: DivDataTag,
        modifier: Modifier = Modifier,
    ) {
        val context = LocalContext.current
        val divContext = Div2Context(
            baseContext = ContextThemeWrapper(context, context.theme),
            configuration = createDivConfiguration(context),
            lifecycleOwner = LocalLifecycleOwner.current
        )

        AndroidView(
            modifier = modifier
                .fillMaxSize()
                .background(JetTodoListTheme.colors.back.primary)
                .padding(
                    WindowInsets.systemBars
                        .only(WindowInsetsSides.Top)
                        .asPaddingValues()
                ),
            factory = {
                Div2View(divContext)
            },
            update = { view ->
                view.setData(data, tag)
            },
            onReset = noopReset,
            onRelease = { view ->
                view.cleanup()
            },
        )
    }

    private fun createDivConfiguration(context: Context): DivConfiguration {
        return DivConfiguration
            .Builder(PicassoDivImageLoader(context))
            .actionHandler(SampleDivActionHandler(onBackPressed))
            .visualErrorsEnabled(true)
            .build()
    }

    private fun JSONObject.asDiv2DataWithTemplates(): DivData {
        val templates = getJSONObject("templates")
        val card = getJSONObject("card")
        val environment = DivParsingEnvironment(ParsingErrorLogger.ASSERT)
        environment.parseTemplates(templates)
        return DivData(environment, card)
    }
}