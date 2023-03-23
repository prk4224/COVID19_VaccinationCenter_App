package com.jaehong.presentation.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MapViewScreen(
    splashViewModel: MapViewModel = hiltViewModel()
) {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ) {

    }
}