package com.jaehong.presentation.ui.map

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.jaehong.domain.model.CenterItem
import com.jaehong.domain.usecase.MapUseCase
import com.jaehong.presentation.util.ArrayConstants.colors
import com.jaehong.presentation.util.ArrayConstants.permissions
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow
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
                mapUseCase.getCenterInfo()
                    .catch { Log.d(TAG, "Get CenterInfo: DB 저장 데이터 불러오기 실패") }
                    .collect { _centerItems.value = it }
            }
            scope.join()
            setCenterTypeColor(centerItems.value)
            scope.cancel()
        }
    }

    fun getCurrentLocation(
        context: Context,
        moveCamera: (LatLng) -> Unit,
    ) {
        if(checkedPermission(context)) return

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener {
                val location = LatLng(it.latitude, it.longitude)
                moveCamera(location)
            }.addOnFailureListener {
                Log.d(TAG, "Get Current Location: 현재위치 불러오기 실패")
            }
    }

    private fun checkedPermission(context: Context): Boolean {
        return if (permissions.all {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }.not()) {
            updatePermissionState(true)
            true
        } else {
            false
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
        val range = getDistance(center,rangeLocation?: return false)
        val distance = getDistance(center,targetLocation)

        return range > distance
    }

    private fun getDistance(center: LatLng, radius: LatLng): Double {
        val earthRadius = 6372.8 * 1000
        val diffLat = Math.toRadians(center.latitude - radius.latitude)
        val diffLon = Math.toRadians(center.longitude - radius.longitude)
        val a = kotlin.math.sin(diffLat / 2).pow(2.0)+
                kotlin.math.sin(diffLon / 2).pow(2.0) *
                kotlin.math.cos(Math.toRadians(radius.latitude)) *
                kotlin.math.cos(Math.toRadians(center.latitude))
        val c = 2 * kotlin.math.asin(kotlin.math.sqrt(a))
        return earthRadius * c
    }
}