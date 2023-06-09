package com.jaehong.covid19_vaccinationcenterapp.di

import android.content.Context
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.jaehong.data.local.network_manager.NetworkManager
import com.jaehong.data.local.network_manager.NetworkManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkManagerModule {

    @Singleton
    @Provides
    fun provideNetworkManager(
        @ApplicationContext context: Context,
        networkRequest: NetworkRequest
    ): NetworkManager = NetworkManagerImpl(context,networkRequest)
    
    @Singleton
    @Provides
    fun provideNetworkRequest(): NetworkRequest {
        return NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
    }
}