package com.jaehong.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CenterInfoEntity (
    @PrimaryKey val id: String,
    val address: String,
    val centerName: String,
    val facilityName: String,
    val phoneNumber: String,
    val updatedAt: String,
    val centerType: String,
)