package com.around_team.todolist.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.around_team.todolist.ui.common.enums.NetworkConnectionState

/**
 * Creates a [ConnectivityManager.NetworkCallback] that invokes the provided [callback] function
 * when network availability changes.
 *
 * @param callback The function to call when network availability changes. It receives a
 * [NetworkConnectionState] parameter indicating the current network state.
 * @return A [ConnectivityManager.NetworkCallback] instance.
 */
fun networkCallback(callback: (NetworkConnectionState) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(NetworkConnectionState.Available)
        }

        override fun onLost(network: Network) {
            callback(NetworkConnectionState.Unavailable)
        }
    }
}

/**
 * Retrieves the current network connectivity state.
 *
 * @param connectivityManager The [ConnectivityManager] instance used to retrieve the network state.
 * @return The current [NetworkConnectionState], indicating whether the network is available or not.
 */
fun getCurrentConnectivityState(connectivityManager: ConnectivityManager): NetworkConnectionState {
    val network = connectivityManager.activeNetwork

    val connected = connectivityManager
        .getNetworkCapabilities(network)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false

    return if (connected) NetworkConnectionState.Available else NetworkConnectionState.Unavailable
}
