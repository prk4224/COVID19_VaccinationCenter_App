package com.jaehong.data.local.datasource

import com.jaehong.data.local.database.VaccinationCenterDataBase
import com.jaehong.data.local.entity.CenterInfoEntity
import com.jaehong.data.local.network_manager.NetworkManager
import com.jaehong.data.util.networkConnectCallback
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val dataBase: VaccinationCenterDataBase,
    private val networkManager: NetworkManager,
) : LocalDataSource {
    override suspend fun getCenterInfo()
            : Flow<List<CenterInfoEntity>> = flow {
        emit(dataBase.centerInfoDao().getCenterItems())
    }

    override suspend fun insertCenterItems(
        centerItems: List<CenterInfoEntity>
    ): Flow<Boolean> = flow {
        emit(dataBase.centerInfoDao().insertCenterInfoWithListTransaction(centerItems))
    }

    override suspend fun observeConnectivityAsFlow():Flow<Boolean> = callbackFlow {
        val connectivityManager = networkManager.getConnectivityManager()
        val callback = networkConnectCallback { result -> trySend(result) }
        val networkRequest = networkManager.getNetworkRequest()

        connectivityManager.registerNetworkCallback(networkRequest,callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}