package com.around_team.todolist.utils


import android.content.Context
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk

/*
class YandexOAuthHelper(context: Context) {
    private val sdk = YandexAuthSdk.create(YandexAuthOptions(context))

    init {
        val launcher = registerForActivityResult(sdk.contract) { result -> handleResult(result) }
        val loginOptions = YandexAuthLoginOptions()
        launcher.launch(loginOptions)
    }

    fun getToken() {
        sdk.getJwt(yandexAuthToken)
    }
    private fun handleResult(result: YandexAuthResult) {
        when (result) {
            is YandexAuthResult.Success -> onSuccessAuth(result.token)
            is YandexAuthResult.Failure -> onProccessError(result.exception)
            YandexAuthResult.Cancelled -> onCancelled()
        }
    }

    fun getTokenLauncher(caller: ActivityResultCaller): ActivityResultLauncher<YandexAuthLoginOptions> {
        return caller.registerForActivityResult(sdk.contract) {
            viewModelScope.launch {
                _tokenState.emit(it)
            }
        }
    }
}*/
