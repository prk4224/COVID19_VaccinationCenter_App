package com.jaehong.data.local.repository

import com.jaehong.data.local.datasource.LocalDataSource
import com.jaehong.data.util.Mapper.domainFromEntity
import com.jaehong.domain.model.CenterItem
import com.jaehong.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
): LocalRepository {

    override suspend fun getCenterInfo(): Flow<List<CenterItem>> = flow {
        localDataSource.getCenterInfo().collect {
            emit(it.map { item -> item.makeCenterItem() })
        }
    }

    override suspend fun insertCenterItems(
        centerItems: List<CenterItem>
    ): Flow<Boolean> = flow {
        localDataSource.insertCenterItems(centerItems.map { it.domainFromEntity() })
            .collect {
                emit(it)
            }
    }

    override suspend fun observeConnectivityAsFlow(): Flow<Boolean> = flow {
        localDataSource.observeConnectivityAsFlow().collect {
            emit(it)
        }
    }
}