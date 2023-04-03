package com.jaehong.covid19_vaccinationcenterapp.di

import android.content.Context
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.jaehong.data.remote.location.LocationManager
import com.jaehong.data.remote.location.LocationManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocationModule {

    @Singleton
    @Provides
    fun provideNetworkManager(
        @ApplicationContext context: Context,
        locationRequest: LocationRequest,
    ): LocationManager = LocationManagerImpl(context,locationRequest)

    @Singleton
    @Provides
    fun provideLocationRequest(): LocationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,Long.MAX_VALUE).build()
}