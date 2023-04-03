package com.jaehong.domain.repository

import com.jaehong.domain.model.CenterItem
import com.jaehong.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    fun getCenterInfo(): Flow<List<CenterItem>>

    fun insertCenterItems(centerItems: List<CenterItem>): Flow<Boolean>

    fun observeConnectivityAsFlow(): Flow<Boolean>
}