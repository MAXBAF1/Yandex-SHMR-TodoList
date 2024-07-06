package com.around_team.todolist.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import com.around_team.todolist.ui.common.enums.NetworkConnectionState
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Modifier extension function to set a background color drawn behind the content.
 *
 * @param color A lambda function that provides the color to be drawn.
 * @return A Modifier with the specified background color.
 */
fun Modifier.background(color: () -> Color): Modifier {
    return drawBehind { drawRect(color()) }
}

/**
 * Casts the body of an HttpResponse to the specified type T, or returns null if the HttpResponse is null or not successful.
 *
 * @return An instance of type T if the casting is successful and HttpResponse is successful (HTTP 200 OK), otherwise null.
 */
suspend inline fun <reified T> HttpResponse?.castOrNull(): T? {
    if (this != null && this.status == HttpStatusCode.OK) {
        return this.body<T>()
    }
    return null
}

/**
 * Observes the connectivity state of the device as a Flow of NetworkConnectionState.
 *
 * @return A Flow emitting NetworkConnectionState representing the current connectivity state of the device.
 */
fun Context.observeConnectivityAsFlow(): Flow<NetworkConnectionState> = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = networkCallback { connectionState ->
        trySend(connectionState)
    }

    val networkRequest = NetworkRequest
        .Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(networkRequest, callback)
    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)

    awaitClose {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}
