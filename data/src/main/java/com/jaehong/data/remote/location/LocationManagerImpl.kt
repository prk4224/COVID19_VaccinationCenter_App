package com.jaehong.data.remote.location

import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.jaehong.data.util.Constants
import com.jaehong.data.util.locationCallback
import com.jaehong.domain.model.Location
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationManagerImpl(
    private val context: Context,
    private val locationRequest: LocationRequest,
) : LocationManager {

    override fun getCurrentLocation(
        updatePermissionState: (Boolean) -> Unit,
    ): Flow<Location> = callbackFlow {

        if (checkedPermission(updatePermissionState)) {
            this.channel
        }

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val callback = locationCallback { location -> trySend(location) }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        )

        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(callback)
        }
    }

    private fun checkedPermission(
        updatePermissionState: (Boolean) -> Unit,
    ): Boolean {
        return if (Constants.permissions.all {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }.not()) {
            updatePermissionState(true)
            true
        } else {
            false
        }
    }
}