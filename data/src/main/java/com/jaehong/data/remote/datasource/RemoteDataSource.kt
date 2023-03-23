package com.jaehong.data.remote.datasource

import com.jaehong.data.remote.model.CenterInfoPage
import com.jaehong.domain.model.result.ApiResult
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun getVaccinationCenters(page: Int): Flow<ApiResult<CenterInfoPage>>
}