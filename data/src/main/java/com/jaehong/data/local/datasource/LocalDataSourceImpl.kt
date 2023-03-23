package com.jaehong.data.local.datasource

import com.jaehong.data.local.database.VaccinationCenterDataBase
import com.jaehong.data.local.entity.CenterInfoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val dataBase: VaccinationCenterDataBase
): LocalDataSource {

    override suspend fun insertCenterItems(
        centerItems: List<CenterInfoEntity>
    ): Flow<Boolean> = flow {
        emit(dataBase.centersDao().insertCenterInfoWithListTransaction(centerItems))
    }

}