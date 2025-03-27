package com.around_team.todolist.ui.screens.about

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.around_team.todolist.di.SharedPreferencesModule
import com.around_team.todolist.ui.screens.settings.models.Theme
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.utils.PreferencesHelper
import com.yandex.div.DivDataTag
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.view2.Div2View
import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div.json.ParsingErrorLogger
import com.yandex.div.picasso.PicassoDivImageLoader
import com.yandex.div2.DivData
import com.yandex.div2.DivVariable
import com.yandex.div2.StrVariable
import org.json.JSONObject


@Composable
fun AboutAppScreen(onBackPressed: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val inputStream = context.assets.open("aboutScreen.json")
    val json = inputStream.bufferedReader().use { it.readText() }
    val data = JSONObject(json).asDiv2DataWithTemplates()
    val theme = getCurrentTheme()
    val editedVariables = data.variables?.toMutableList()?.map {
        if (it.value() is StrVariable) {
            val strVariable = it.value() as StrVariable
            val newStrVariable = if (strVariable.name == "app_theme") {
                strVariable.copy(value = if (theme == Theme.Sun) "light" else "dark")
            } else strVariable

            DivVariable.Str(newStrVariable)
        } else it

    }
    val editedData = data.copy(variables = editedVariables)

    DivView(
        data = editedData,
        tag = DivDataTag("div2"),
        onBackPressed = onBackPressed,
        modifier = modifier,
    )
}

@Composable
private fun DivView(
    data: DivData,
    tag: DivDataTag,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val divContext = Div2Context(
        baseContext = ContextThemeWrapper(context, context.theme),
        configuration = createDivConfiguration(context, onBackPressed),
        lifecycleOwner = LocalLifecycleOwner.current
    )

    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .background(JetTodoListTheme.colors.back.primary)
            .padding(
                WindowInsets.systemBars.only(WindowInsetsSides.Top).asPaddingValues()
            ),
        factory = {
            Div2View(divContext)
        },
        update = { view ->
            view.setData(data, tag)
        },
        onReset = {},
        onRelease = { view ->
            view.cleanup()
        },
    )
}

@Composable
private fun getCurrentTheme(): Theme {
    val sharedPreferences = LocalContext.current.getSharedPreferences(
        SharedPreferencesModule.KEY, Context.MODE_PRIVATE
    )
    val preferencesHelper = PreferencesHelper(sharedPreferences)
    return preferencesHelper.getSelectedTheme()
        ?: if (isSystemInDarkTheme()) Theme.Moon else Theme.Sun
}

private fun createDivConfiguration(context: Context, onBackPressed: () -> Unit): DivConfiguration {
    return DivConfiguration.Builder(PicassoDivImageLoader(context))
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
