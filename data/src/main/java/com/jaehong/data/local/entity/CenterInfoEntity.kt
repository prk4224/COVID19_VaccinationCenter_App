package com.jaehong.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jaehong.domain.model.CenterItem

@Entity
data class CenterInfoEntity (
    @PrimaryKey val id: String,
    val address: String,
    val centerName: String,
    val facilityName: String,
    val phoneNumber: String,
    val updatedAt: String,
    val centerType: String,
) {
    fun mappingCenterInfoEntityFromCenterItem(): CenterItem {
        return CenterItem(
            id = this.id,
            address = this.address,
            centerName = this.centerName,
            facilityName = this.facilityName,
            phoneNumber = this.phoneNumber,
            updatedAt = this.updatedAt,
            lat = this.lat,
            lng = this.lng,
            centerType = this.centerType,
        )
    }
}