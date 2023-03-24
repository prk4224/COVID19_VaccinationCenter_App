package com.jaehong.presentation.ui.map

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.jaehong.domain.model.CenterItem
import com.jaehong.domain.usecase.MapUseCase
import com.jaehong.presentation.util.ArrayConstants.permissions
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapUseCase: MapUseCase
): ViewModel() {


    private val _centerItems = MutableStateFlow(listOf<CenterItem>())
    val centerItems = _centerItems.asStateFlow()

    private val _permissionState = MutableStateFlow(false)
    val permissionState = _permissionState.asStateFlow()

    init {
        getCenterInfo()
    }

    private fun getCenterInfo() {
        viewModelScope.launch {
            mapUseCase.getCenterInfo()
                .catch { }
                .collect {
                    _centerItems.value = it
                }
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
                moveCamera( LatLng(it.latitude, it.longitude))
            }.addOnFailureListener {
                Log.d("Get Current Location", "현재위치 불러오기 실패")
            }
    }

    private fun checkedPermission(
        context: Context,
    ): Boolean {
        updatePermissionState(false)
        return if (permissions.all {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }.not()
        ) {
            updatePermissionState(true)
            true
        } else {
            false
        }
    }

    private fun updatePermissionState(value: Boolean) {
        _permissionState.value = value
    }
}