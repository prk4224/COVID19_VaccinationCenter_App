package com.jaehong.data.remote.datasource

import com.jaehong.data.remote.model.CenterInfoPage
import com.jaehong.data.remote.service.VaccinationCenterService
import com.jaehong.data.util.Constants.API_KEY
import com.jaehong.data.util.safeApiCall
import com.jaehong.domain.model.result.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val service: VaccinationCenterService
): RemoteDataSource {

    override suspend fun getVaccinationCenters(
        page: Int,
    ): Flow<ApiResult<CenterInfoPage>> = flow {
        emit( safeApiCall { service.getVaccinationCenters(API_KEY,page) } )
    }
}