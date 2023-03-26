package com.jaehong.covid19_vaccinationcenterapp.di

import com.jaehong.presentation.ui.navigation.VaccinationAppNavigator
import com.jaehong.presentation.ui.navigation.VaccinationAppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Singleton
    @Binds
    fun bindVaccinationAppNavigator(vaccinationAppNavigatorImpl: VaccinationAppNavigatorImpl): VaccinationAppNavigator
}