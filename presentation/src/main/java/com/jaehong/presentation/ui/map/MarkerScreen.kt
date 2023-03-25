package com.jaehong.presentation.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jaehong.domain.model.CenterItem
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.overlay.Marker

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MarkerScreen(
    item: CenterItem,
    state: Boolean,
    color: Color,
    onClick: (Marker) -> Boolean,
    moveCamera: (LatLng) -> Unit,
) {
    val location = LatLng(item.lat.toDouble(), item.lng.toDouble())

    Marker(
        state = MarkerState(position = location),
        captionText = item.centerName,
        iconTintColor = if(state) Color.Red else color,
    ) {
        moveCamera(location)
        onClick(it)
    }
}
