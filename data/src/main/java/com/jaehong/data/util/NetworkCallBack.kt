package com.jaehong.data.util

import android.net.ConnectivityManager
import android.net.Network

fun networkCallback(callback: (Boolean) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(true)
        }

        override fun onLost(network: Network) {
            callback(false)
        }
    }
}