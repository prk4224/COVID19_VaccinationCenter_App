package com.jaehong.data.remote.location

import com.jaehong.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationManager {

    fun getCurrentLocation(
        updatePermissionState: (Boolean) -> Unit,
    ): Flow<Location>
}