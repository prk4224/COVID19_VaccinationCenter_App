package com.jaehong.data.remote.repository

import com.jaehong.data.remote.datasource.RemoteDataSource
import com.jaehong.data.util.Mapper.dataFromDomain
import com.jaehong.domain.model.CenterItem
import com.jaehong.domain.model.Location
import com.jaehong.domain.model.result.ApiResult
import com.jaehong.domain.repository.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : RemoteRepository {

    override fun getCenterInfo(
        page: Int
    ): Flow<ApiResult<List<CenterItem>>> = flow {
        remoteDataSource.getCenterInfo(page).map {
            it.dataFromDomain()
        }.collect {
            emit(it)
        }
    }

    override fun getCurrentLocation(
        updatePermissionState: (Boolean) -> Unit
    ): Flow<Location> = flow {
        remoteDataSource.getCurrentLocation(updatePermissionState).collect {
            emit(it)
        }
    }
}