package com.jaehong.data.local.network_manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import com.jaehong.data.util.networkConnectCallback
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkManagerImpl(
    private val context: Context,
    private val networkRequest: NetworkRequest,
    ): NetworkManager {
    override fun observeConnectivityAsFlow(): Flow<Boolean> = callbackFlow {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val callback = networkConnectCallback { result -> trySend(result) }

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}