package com.jaehong.data.local.datasource

import com.jaehong.data.local.database.VaccinationCenterDataBase
import com.jaehong.data.local.entity.CenterInfoEntity
import com.jaehong.data.local.network_manager.NetworkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val dataBase: VaccinationCenterDataBase,
    private val networkManager: NetworkManager,
) : LocalDataSource {
    override fun getCenterInfo(
    ): Flow<List<CenterInfoEntity>> = flow {
        emit(dataBase.centerInfoDao().getCenterItems())
    }

    override fun insertCenterItems(
        centerItems: List<CenterInfoEntity>
    ): Flow<Boolean> = flow {
        emit(dataBase.centerInfoDao().insertCenterInfoWithListTransaction(centerItems))
    }

    override fun observeConnectivityAsFlow(
    ): Flow<Boolean> = flow {
        networkManager.observeConnectivityAsFlow().collect {
            emit(it)
        }
    }
}