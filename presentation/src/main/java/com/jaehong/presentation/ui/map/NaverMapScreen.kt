package com.jaehong.presentation.ui.map

import android.graphics.PointF
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jaehong.domain.model.CenterItem
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.*

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMapScreen(
    centerItems: List<CenterItem>,
    mapProperties: MapProperties,
    mapUiSettings: MapUiSettings,
    cameraPositionState: CameraPositionState,
    onMapClick: (PointF, LatLng) -> Unit,
    initPosition: () -> Unit,
    marker: @Composable (CenterItem) -> Unit,
    checkedRangeForMarker: (LatLng,LatLng?,LatLng) -> Boolean,
) {
    NaverMap(
        properties = mapProperties,
        uiSettings = mapUiSettings,
        cameraPositionState = cameraPositionState,
        onMapLoaded = { initPosition() },
        onMapClick = { point, latLng -> onMapClick(point,latLng) }
    ) {
        centerItems.forEach {
            if(checkedRangeForMarker(
                    cameraPositionState.position.target,
                    cameraPositionState.contentBounds?.northEast,
                    LatLng(it.lat.toDouble(),it.lng.toDouble()))
            ) {
                marker(it)
            }
        }
    }
}