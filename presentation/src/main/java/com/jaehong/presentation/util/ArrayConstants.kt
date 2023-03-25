package com.jaehong.presentation.util

import android.Manifest
import androidx.compose.ui.graphics.Color

object ArrayConstants {
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val colors = arrayOf(
        Color.Black,
        Color.Blue,
        Color.DarkGray,
        Color.LightGray,
        Color.Cyan,
        Color.Green,
        Color.Yellow,
        Color.Magenta,
        Color.Gray,
    )
}