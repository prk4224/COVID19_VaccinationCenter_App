package com.jaehong.presentation.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoadingScreen(
    loadingValue: Int,
    loadingWidth: Int,
    startLoading: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "$loadingValue %",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(bottom = 50.dp)
        )

        Box(
            modifier = Modifier
                .background(Color.LightGray, RoundedCornerShape(10.dp))
                .width(loadingWidth.dp)
                .height(20.dp)
                .align(Alignment.Center),
        )
    }
    startLoading()

}