package com.jaehong.data.local.datasource

import com.jaehong.data.local.entity.CenterInfoEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getCenterInfo(): Flow<List<CenterInfoEntity>>

    suspend fun insertCenterItems(centerItems: List<CenterInfoEntity>): Flow<Boolean>
}