package com.jaehong.data.local.dao

import androidx.room.*
import com.jaehong.data.local.entity.CenterInfoEntity

@Dao
interface CenterInfoDao {

    @Query("SELECT * FROM CenterInfoEntity")
    suspend fun getCenterItems(): List<CenterInfoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCenterInfo(info: CenterInfoEntity): Long

    @Transaction
    suspend fun insertCenterInfoWithListTransaction(centerItems: List<CenterInfoEntity>): Boolean {
        var result = 0
        centerItems.forEach {
            if(insertCenterInfo(it) > 0) result++
        }
        return result == centerItems.size
    }

}