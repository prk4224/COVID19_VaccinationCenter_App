package com.jaehong.data.local.network_manager

import kotlinx.coroutines.flow.Flow

interface NetworkManager {

    fun observeConnectivityAsFlow(): Flow<Boolean>
}