package com.jaehong.data.util

import android.Manifest
import com.jaehong.data.BuildConfig

object Constants {
    const val API_KEY = BuildConfig.API_KEY

    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}