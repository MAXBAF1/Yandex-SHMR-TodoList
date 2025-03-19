import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    onColorChange: (Color) -> Unit,
    boxWidth: Dp = 1000.dp,
    boxHeight: Dp = 300.dp
) {
    var brightness by remember { mutableFloatStateOf(1f) }
    var selectorPosition by remember { mutableStateOf(Offset(0f, 0f)) }
    val horizontalColors = listOf(
        Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red
    )
    val density = LocalDensity.current.density

    val currentColor = remember(selectorPosition, brightness) {
        val proportionX = selectorPosition.x / (boxWidth.value)
        val proportionY = selectorPosition.y / (boxHeight.value * density)

        val horizontalColor = lerpGradient(horizontalColors, proportionX)
        val mixedColor = lerp(Color.White, horizontalColor, proportionY)

        val finalColor = mixedColor.copy(
            red = (mixedColor.red * brightness).coerceIn(0f, 1f),
            green = (mixedColor.green * brightness).coerceIn(0f, 1f),
            blue = (mixedColor.blue * brightness).coerceIn(0f, 1f)
        )

        onColorChange(finalColor)
        finalColor
    }

    Column(modifier = modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(currentColor, shape = CircleShape)
        )

        Slider(value = brightness, onValueChange = { brightness = it })

        Box(modifier = Modifier
            .height(boxHeight)
            .width(boxWidth)
            .background(
                brush = Brush.horizontalGradient(
                    colors = horizontalColors.map { lerp(Color.Black, it, brightness) }
                )
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color.Transparent).map {
                        it.copy(
                            red = (it.red * brightness).coerceIn(0f, 1f),
                            green = (it.green * brightness).coerceIn(0f, 1f),
                            blue = (it.blue * brightness).coerceIn(0f, 1f)
                        )
                    }
                )
            )
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    selectorPosition = Offset(
                        x = change.position.x.coerceIn(0F, boxWidth.toPx()),
                        y = change.position.y.coerceIn(0F, boxHeight.toPx())
                    )
                }
            })
    }
}

fun lerpGradient(colors: List<Color>, proportion: Float): Color {
    val count = colors.size - 1
    val index = (proportion * count).toInt().coerceIn(0, count - 1)
    val localRatio = (proportion * count) - index
    return lerp(colors[index], colors[index + 1], localRatio)
}

fun lerp(color1: Color, color2: Color, ratio: Float): Color {
    return Color(
        red = (color1.red + (color2.red - color1.red) * ratio).coerceIn(0f, 1f),
        green = (color1.green + (color2.green - color1.green) * ratio).coerceIn(0f, 1f),
        blue = (color1.blue + (color2.blue - color1.blue) * ratio).coerceIn(0f, 1f),
        alpha = color1.alpha + (color2.alpha - color1.alpha) * ratio
    )
}
