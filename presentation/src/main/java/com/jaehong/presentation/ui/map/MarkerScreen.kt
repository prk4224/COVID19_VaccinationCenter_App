package com.jaehong.presentation.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    color: Color,
    onClick: (Marker) -> Boolean,
) {
    val state = remember {
        mutableStateOf(false)
    }

    Marker(
        state = MarkerState(position = LatLng(item.lat.toDouble(), item.lng.toDouble())),
        captionText = item.centerName,
        iconTintColor = if(state.value) color else Color.Black,
    ) {
        val tmep  = onClick(it)
        state.value = tmep
        tmep
    }
}