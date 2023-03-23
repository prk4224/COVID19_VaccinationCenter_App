package com.jaehong.covid19_vaccinationcenterapp.di

import android.content.Context
import androidx.room.Room
import com.jaehong.data.local.dao.CenterInfoDao
import com.jaehong.data.local.database.VaccinationCenterDataBase
import com.jaehong.data.local.database.VaccinationCenterDataBase.Companion.VACCINATION_CENTER_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Singleton
    @Provides
    fun provideVaccinationCenterDataBase(
        @ApplicationContext context: Context
    ): VaccinationCenterDataBase {
        return Room.databaseBuilder(
            context, VaccinationCenterDataBase::class.java, VACCINATION_CENTER_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideCentersDao(
        dataBase: VaccinationCenterDataBase
    ): CenterInfoDao = dataBase.centersDao()
}