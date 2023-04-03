package com.jaehong.data.remote.datasource

import com.jaehong.data.remote.model.CenterInfoPage
import com.jaehong.domain.model.Location
import com.jaehong.domain.model.result.ApiResult
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    fun getCenterInfo(page: Int): Flow<ApiResult<CenterInfoPage>>

    fun getCurrentLocation(updatePermissionState: (Boolean) -> Unit): Flow<Location>
}