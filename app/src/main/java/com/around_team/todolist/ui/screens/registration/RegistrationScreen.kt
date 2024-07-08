package com.around_team.todolist.ui.screens.registration

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.around_team.todolist.ui.screens.registration.models.RegistrationEvent
import com.around_team.todolist.ui.screens.registration.views.SignInWithYandexIdButton
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk

/**
 * A screen class for handling user registration.
 *
 * @property viewModel The ViewModel instance that handles the business logic and state for the registration screen.
 * @property toNextScreen A lambda function to navigate to the next screen after successful registration.
 */
class RegistrationScreen(
    private val viewModel: RegistrationViewModel,
    private val toNextScreen: () -> Unit,
) {

    @Composable
    fun Create() {
        val viewState by viewModel
            .getViewState()
            .collectAsStateWithLifecycle()
        val context = LocalContext.current
        val sdk = YandexAuthSdk.create(YandexAuthOptions(context))
        val launcher = rememberLauncherForActivityResult(sdk.contract) { result ->
            viewModel.obtainEvent(RegistrationEvent.HandleResult(result))
        }
        val options = YandexAuthLoginOptions()

        if (viewState.message != null) {
            LaunchedEffect(viewState.message) {
                Toast
                    .makeText(context, viewState.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        if (viewState.toNextScreen) toNextScreen()

        Surface(modifier = Modifier.fillMaxSize(), color = JetTodoListTheme.colors.back.primary) {
            Box(
                modifier = Modifier.padding(horizontal = 50.dp), contentAlignment = Alignment.Center
            ) {
                SignInWithYandexIdButton(onClick = { launcher.launch(options) })
            }
        }
    }
}