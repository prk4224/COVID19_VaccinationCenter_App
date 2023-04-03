package com.jaehong.domain.usecase

import com.jaehong.domain.model.CenterItem
import com.jaehong.domain.model.Location
import com.jaehong.domain.repository.LocalRepository
import com.jaehong.domain.repository.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MapUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
) {
    suspend operator fun invoke() : Flow<List<CenterItem>> = flow {
        localRepository.getCenterInfo().collect {
            emit(it)
        }
    }

    fun getCurrentLocation(
        updatePermissionState: (Boolean) -> Unit
    ): Flow<Location> = flow {
        remoteRepository.getCurrentLocation(updatePermissionState).collect {
            emit(it)
        }
    }
}