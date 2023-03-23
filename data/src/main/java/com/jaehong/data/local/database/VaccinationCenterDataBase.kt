package com.jaehong.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jaehong.data.local.dao.CenterInfoDao
import com.jaehong.data.local.entity.CenterInfoEntity

@Database(
    entities = [
        CenterInfoEntity::class
    ], version = 1
)
abstract class VaccinationCenterDataBase : RoomDatabase() {

    abstract fun centersDao(): CenterInfoDao

    companion object {
        const val VACCINATION_CENTER_NAME = "VaccinationCenterApp.db"
    }
}