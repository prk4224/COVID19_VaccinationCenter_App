package com.jaehong.presentation.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaehong.presentation.ui.navigation.Destination
import com.jaehong.presentation.ui.navigation.VaccinationAppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val vaccinationAppNavigator: VaccinationAppNavigator,
): ViewModel() {

    init {
        onNavigatePopSplash()
    }

    private fun onNavigatePopSplash() {
        viewModelScope.launch {
            vaccinationAppNavigator.navigateBack(Destination.Splash())
        }
    }
}