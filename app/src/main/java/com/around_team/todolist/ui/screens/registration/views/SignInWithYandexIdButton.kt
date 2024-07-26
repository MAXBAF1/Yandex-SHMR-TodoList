package com.around_team.todolist.ui.screens.registration.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.JetTodoListTheme

/**
 * A composable function that creates a button for signing in with Yandex ID.
 *
 * @param onClick A lambda function to be executed when the button is clicked.
 * @param modifier A modifier to be applied to the button, defaulting to an empty modifier.
 */
@Composable
fun SignInWithYandexIdButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = JetTodoListTheme.colors.yandex.back),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_yandex),
                contentDescription = null
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.sign_in_with_yandex_id),
                color = JetTodoListTheme.colors.yandex.label,
                style = JetTodoListTheme.typography.headline
            )
        }
    }
}

@Preview
@Composable
private fun SignInWithYandexIdButtonPreview() {
    SignInWithYandexIdButton({})
}