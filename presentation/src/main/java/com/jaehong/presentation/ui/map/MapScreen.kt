package com.jaehong.presentation.ui.map

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jaehong.domain.model.CenterItem
import com.jaehong.presentation.util.ArrayConstants.permissions
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalNaverMapApi::class)
@Composable
fun MapViewScreen(
    mapViewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val centerItems = mapViewModel.centerItems.collectAsState().value
    val permissionState = mapViewModel.permissionState.collectAsState().value
    val colorMap = mapViewModel.colorHashMap.collectAsState().value

    val selectedItem = remember {
        mutableStateOf<CenterItem?>(null)
    }

    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(37.532600, 127.024612), 15.0)
    }

    val moveCamera: (LatLng) -> Unit = {
        cameraPositionState.move(CameraUpdate.scrollTo(it))
    }

    val mapProperties by remember {
        mutableStateOf( MapProperties(maxZoom = 25.0, minZoom = 5.0,) )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isLocationButtonEnabled = false)
        )
    }

    // Launcher
    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        mapViewModel.updatePermissionState(false)
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            mapViewModel.getCurrentLocation(
                context = context,
                moveCamera = { position -> moveCamera(position) }
            )
        }
    }

    if(permissionState) {
        launcherMultiplePermissions.launch(permissions)
    }

    Box(Modifier.fillMaxSize()) {
        NaverMapScreen(
            centerItems = centerItems,
            mapProperties = mapProperties,
            mapUiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
            initPosition = { mapViewModel.getCurrentLocation(
                context = context,
                moveCamera = { position -> moveCamera(position) }
            ) },
            onMapClick = { pointF, latLng -> selectedItem.value = null },
            marker = { item ->
                MarkerScreen(
                    item = item,
                    color = colorMap[item.centerType] ?: throw IllegalArgumentException("Color Type Error"),
                    state = selectedItem.value == item,
                    onClick = {
                        if (selectedItem.value == item) {
                            selectedItem.value = null
                            false
                        } else {
                            selectedItem.value = item
                            true
                        }
                    },
                    moveCamera = { latLng -> moveCamera(latLng) },
                )
            },
        )
        BottomSheetScreen(
            selectedItem = selectedItem.value,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )

        CurrentPositionButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(15.dp),
            onClick = {
                mapViewModel.getCurrentLocation(
                context = context,
                moveCamera = { position -> moveCamera(position) }
            ) }
        )
    }
}