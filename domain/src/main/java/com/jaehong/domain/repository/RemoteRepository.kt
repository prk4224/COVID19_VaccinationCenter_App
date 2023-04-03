package com.jaehong.domain.repository

import com.jaehong.domain.model.CenterItem
import com.jaehong.domain.model.Location
import com.jaehong.domain.model.result.ApiResult
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {

    fun getCenterInfo(page: Int): Flow<ApiResult<List<CenterItem>>>

    fun getCurrentLocation(updatePermissionState: (Boolean) -> Unit): Flow<Location>
}