package com.jaehong.covid19_vaccinationcenterapp.di

import com.jaehong.data.local.repository.LocalRepositoryImpl
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
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindRemoteRepository(remoteRepositoryImpl: RemoteRepositoryImpl): RemoteRepository

    @Singleton
    @Binds
    fun bindLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository

}