package com.jaehong.covid19_vaccinationcenterapp.di

import com.jaehong.data.local.datasource.LocalDataSource
import com.jaehong.data.local.datasource.LocalDataSourceImpl
import com.jaehong.data.local.repository.LocalRepositoryImpl
import com.jaehong.data.remote.datasource.RemoteDataSource
import com.jaehong.data.remote.datasource.RemoteDataSourceImpl
import com.jaehong.data.remote.repository.RemoteRepositoryImpl
import com.jaehong.domain.repository.LocalRepository
import com.jaehong.domain.repository.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Singleton
    @Binds
    fun bindRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Singleton
    @Binds
    fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

}