package com.jaehong.data.remote.model

import com.jaehong.domain.model.CenterItem

data class CenterInfoPage(
    val page: Int,
    val data: List<CenterInfo>,
) {
    fun mappingCenterInfoPageFromCenterItems(): List<CenterItem> {
        return this.data.map {
            CenterItem(
                id = it.id,
                address = it.address,
                centerName = it.centerName,
                facilityName = it.facilityName,
                phoneNumber = it.phoneNumber,
                updatedAt = it.updatedAt,
                centerType = it.centerType,
                )
        }
    }
}