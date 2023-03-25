package com.jaehong.data.local.network_manager

import android.net.ConnectivityManager
import android.net.NetworkRequest

interface NetworkManager {

    fun getConnectivityManager(): ConnectivityManager

    fun getNetworkRequest(): NetworkRequest

}