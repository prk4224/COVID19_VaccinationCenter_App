package com.jaehong.presentation.ui.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CurrentLocationButton(
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Icon(imageVector =  Icons.Filled.LocationOn,
        tint = Color.Red,
        contentDescription = null,
        modifier = modifier
            .size(45.dp)
            .clickable { onClick() }
    )
}