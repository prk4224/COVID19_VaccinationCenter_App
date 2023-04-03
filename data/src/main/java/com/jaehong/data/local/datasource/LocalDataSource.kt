package com.jaehong.data.local.datasource

import com.jaehong.data.local.entity.CenterInfoEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getCenterInfo(): Flow<List<CenterInfoEntity>>

    fun insertCenterItems(centerItems: List<CenterInfoEntity>): Flow<Boolean>

    fun observeConnectivityAsFlow(): Flow<Boolean>
}