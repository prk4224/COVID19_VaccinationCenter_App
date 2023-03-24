package com.jaehong.presentation.ui.map

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetScreen(
    modifier: Modifier,
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
        modifier = modifier,
        enter = slideInVertically (initialOffsetY = {
            +it
        }),
        exit = slideOutVertically(targetOffsetY = {
            +it
        }),
    ) {
        Box(modifier = modifier
            .height(200.dp)
            .background(Color.White)
            .border(BorderStroke(1.dp, Color.LightGray),RoundedCornerShape(topEndPercent = 10, topStartPercent = 10))
        ) {

        }
    }
}