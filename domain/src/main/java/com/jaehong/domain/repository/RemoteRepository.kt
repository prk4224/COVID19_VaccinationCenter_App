package com.jaehong.domain.repository

import com.jaehong.domain.model.CenterItem
import com.jaehong.domain.model.result.ApiResult
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {

    suspend fun getVaccinationCenters(page: Int): Flow<ApiResult<List<CenterItem>>>


}