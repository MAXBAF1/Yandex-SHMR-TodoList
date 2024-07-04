package com.around_team.todolist.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import com.around_team.todolist.ui.common.models.TodoItem
import com.around_team.todolist.ui.common.enums.NetworkConnectionState
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun Modifier.background(color: () -> Color): Modifier {
    return drawBehind { drawRect(color()) }
}

fun List<TodoItem>.find(otherTodoId: String): Pair<Int, TodoItem?> {
    var index = -1
    var foundTodo: TodoItem? = null
    this.forEachIndexed { i, todo ->
        if (todo.id == otherTodoId) {
            index = i
            foundTodo = todo
            return@forEachIndexed
        }
    }

    return index to foundTodo
}

suspend inline fun <reified T> HttpResponse?.castOrNull(): T? {
    if (this != null && this.status == HttpStatusCode.OK) {
        return this.body<T>()
    }

    return null
}

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