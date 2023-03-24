package com.jaehong.presentation.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jaehong.domain.model.CenterItem
import com.naver.maps.map.compose.*

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMapScreen(
    centerItems: List<CenterItem>,
    mapProperties: MapProperties,
    mapUiSettings: MapUiSettings,
    cameraPositionState: CameraPositionState,
    marker: @Composable (CenterItem,Color) -> Unit,
    ) {
    NaverMap(
        properties = mapProperties,
        uiSettings = mapUiSettings,
        cameraPositionState = cameraPositionState
    ) {
        centerItems.forEach {
            marker(it, Color.Red)
        }
    }
}