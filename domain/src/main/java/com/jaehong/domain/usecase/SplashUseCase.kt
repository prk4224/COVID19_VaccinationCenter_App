package com.jaehong.domain.usecase

import com.jaehong.domain.model.CenterItem
import com.jaehong.domain.model.result.ApiResult
import com.jaehong.domain.repository.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SplashUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
) {

    suspend fun getVaccinationCenters(
        page: Int,
    ): Flow<ApiResult<List<CenterItem>>> = flow {
        remoteRepository.getVaccinationCenters(page)
            .collect { emit(it) }
    }
}