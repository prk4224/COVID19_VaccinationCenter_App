package com.jaehong.presentation.ui.home

import androidx.lifecycle.ViewModel
import com.jaehong.presentation.ui.navigation.VaccinationAppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    vaccinationAppNavigator: VaccinationAppNavigator
) : ViewModel() {
    val navigationChannel = vaccinationAppNavigator.navigationChannel
}