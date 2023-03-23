package com.jaehong.data.util

import com.jaehong.data.local.entity.CenterInfoEntity
import com.jaehong.data.remote.model.CenterInfoPage
import com.jaehong.domain.model.CenterItem
import com.jaehong.domain.model.result.ApiResult

object Mapper {

    fun ApiResult<CenterInfoPage>.dataFromDomain(): ApiResult<List<CenterItem>> {
        return when(this) {
            is ApiResult.Success -> {
                ApiResult.Success(this.data.mappingCenterInfoPageFromCenterItems())
            }
            is ApiResult.Error -> {
                ApiResult.Error(this.exception)
            }
        }
    }

    fun CenterItem.domainFromEntity(): CenterInfoEntity {
        return CenterInfoEntity(
            id = this.id,
            address = this.address,
            centerName = this.centerName,
            facilityName = this.facilityName,
            phoneNumber = this.phoneNumber,
            updatedAt = this.updatedAt,
            centerType = this.centerType,
        )
    }
}