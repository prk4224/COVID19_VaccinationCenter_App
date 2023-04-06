package com.jaehong.presentation.ui.map

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaehong.domain.model.CenterItem
import com.jaehong.domain.usecase.MapUseCase
import com.jaehong.presentation.util.ArrayConstants.colors
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapUseCase: MapUseCase,
): ViewModel() {

    private val TAG = "MapViewModel"

    private val _centerItems = MutableStateFlow(listOf<CenterItem>())
    val centerItems = _centerItems.asStateFlow()

    private val _permissionState = MutableStateFlow(false)
    val permissionState = _permissionState.asStateFlow()

    private val _colorHashMap = MutableStateFlow(hashMapOf<String, Color>())
    val colorHashMap = _colorHashMap.asStateFlow()

    init {
        getCenterInfo()
    }

    private fun getCenterInfo() {
        viewModelScope.launch {
            val scope = launch {
                mapUseCase()
                    .catch { Log.d(TAG, "Get CenterInfo: ${it.message}") }
                    .collect { _centerItems.value = it }
            }
            scope.join()
            setCenterTypeColor(centerItems.value)
        }
    }

    fun getCurrentLocation(
        moveCamera: (LatLng) -> Unit,
    ) {
        viewModelScope.launch {
            mapUseCase.getCurrentLocation { state -> updatePermissionState(state) }
                .catch { Log.d(TAG, "Get Current Location : ${it.message}") }
                .collect { moveCamera(LatLng(it.lat, it.lng)) }
        }
    }

    fun updatePermissionState(value: Boolean) {
        _permissionState.value = value
    }

    private fun setCenterTypeColor(items: List<CenterItem>) {
        items.forEach { item ->
            if (colorHashMap.value.containsKey(item.centerType).not()) {
                if (colors.size > colorHashMap.value.size) {
                    colorHashMap.value[item.centerType] = colors[colorHashMap.value.size]
                } else {
                    colorHashMap.value[item.centerType] = makeRandomColor()
                }
            }
        }
    }

    private fun makeRandomColor(): Color {
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)
        return Color(red, green, blue)
    }

    fun checkedRangeForMarker(
        center: LatLng,
        rangeLocation: LatLng?,
        targetLocation: LatLng
    ): Boolean {
        val range = center.distanceTo(rangeLocation ?: return false)
        val distance = center.distanceTo(targetLocation)

        return range > distance
    }
}