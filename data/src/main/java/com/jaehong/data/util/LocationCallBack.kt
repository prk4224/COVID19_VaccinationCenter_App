package com.jaehong.data.util

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.jaehong.domain.model.Location

fun locationCallback(callback: (Location) -> Unit): LocationCallback {
    return object : LocationCallback() {
        override fun onLocationResult(locationRequest: LocationResult) {
            callback(Location(locationRequest.lastLocation?.latitude?:return,locationRequest.lastLocation?.longitude?:return))
        }
    }
}