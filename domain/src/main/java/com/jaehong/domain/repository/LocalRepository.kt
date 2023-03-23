package com.jaehong.domain.repository

import com.jaehong.domain.model.CenterItem
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    suspend fun insertCenterItems(centerItems: List<CenterItem>): Flow<Boolean>
}