package com.around_team.todolist.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun TodoListTheme(
    style: JetTodoListStyle = JetTodoListStyle.Base,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = when (darkTheme) {
        true -> {
            when (style) {
                JetTodoListStyle.Base -> baseDarkPalette
            }
        }

        false -> {
            when (style) {
                JetTodoListStyle.Base -> baseLightPalette
            }
        }
    }

    CompositionLocalProvider(
        LocalJetAroundColors provides colors,
        LocalJetTodoListTypography provides typography,
        content = content
    )
}

@Preview(widthDp = 410)
@Composable
private fun TextStylesPreview() {
    TodoListTheme {
        Column(
            modifier = Modifier
                .background(JetTodoListTheme.colors.back.secondary)
                .padding(32.dp)

        ) {
            TextStyleText(
                modifier = Modifier.padding(bottom = 32.dp),
                name = "Large title",
                style = JetTodoListTheme.typography.largeTitle,
            )
            TextStyleText(
                modifier = Modifier.padding(bottom = 32.dp),
                name = "Title",
                style = JetTodoListTheme.typography.title,
            )
            TextStyleText(
                modifier = Modifier.padding(bottom = 32.dp),
                name = "Headline",
                style = JetTodoListTheme.typography.headline,
            )
            TextStyleText(
                modifier = Modifier.padding(bottom = 32.dp),
                name = "Body",
                style = JetTodoListTheme.typography.body,
            )
            TextStyleText(
                modifier = Modifier.padding(bottom = 32.dp),
                name = "Subhead",
                style = JetTodoListTheme.typography.subhead,
            )
            TextStyleText(
                name = "Footnote",
                style = JetTodoListTheme.typography.footnote,
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, widthDp = 1440)
@Composable
private fun PaletteLightPreview() {
    TodoListTheme {
        Palette()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, widthDp = 1440)
@Composable
private fun PaletteNightPreview() {
    TodoListTheme {
        Palette()
    }
}

@Composable
private fun Palette(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(bottom = 30.dp)) {
            ColorCard(JetTodoListTheme.colors.support.separator, "Support / Separator")
            ColorCard(JetTodoListTheme.colors.support.overlay, "Support / Overlay")
            ColorCard(JetTodoListTheme.colors.support.navbarBlur, "Support / Navbar Blur")
        }
        Row(modifier = Modifier.padding(bottom = 30.dp)) {
            ColorCard(JetTodoListTheme.colors.label.primary, "Label / Primary")
            ColorCard(JetTodoListTheme.colors.label.secondary, "Label / Secondary")
            ColorCard(JetTodoListTheme.colors.label.tertiary, "Label / Tertiary")
            ColorCard(JetTodoListTheme.colors.label.disable, "Label / Disable")
        }
        Row(modifier = Modifier.padding(bottom = 30.dp)) {
            ColorCard(JetTodoListTheme.colors.colors.red, "Colors / Red")
            ColorCard(JetTodoListTheme.colors.colors.green, "Colors / Green")
            ColorCard(JetTodoListTheme.colors.colors.blue, "Colors / Blue")
            ColorCard(JetTodoListTheme.colors.colors.gray, "Colors / Gray")
            ColorCard(JetTodoListTheme.colors.colors.grayLight, "Colors / Gray Light")
        }
        Row {
            ColorCard(JetTodoListTheme.colors.back.iosPrimary, "Back / Ios Primary")
            ColorCard(JetTodoListTheme.colors.back.primary, "Back / Primary")
            ColorCard(JetTodoListTheme.colors.back.secondary, "Back / Secondary")
            ColorCard(JetTodoListTheme.colors.back.elevated, "Back / Elevated")
        }
    }
}


@OptIn(ExperimentalStdlibApi::class)
@Composable
private fun ColorCard(color: Color, text: String, modifier: Modifier = Modifier) {
    val textColor = if (color.luminance() > 0.6) Color.Black else Color.White

    Column(
        modifier = modifier
            .size(240.dp, 100.dp)
            .background(color)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(text = text, color = textColor, fontSize = 10.sp)
        Text(
            text = "#${color.toArgb().toHexString().uppercase()}",
            color = textColor,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun TextStyleText(name: String, style: TextStyle, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = "$name — ${style.fontSize.value.toInt()}/${style.lineHeight.value.toInt()}",
        style = style,
    )
}

