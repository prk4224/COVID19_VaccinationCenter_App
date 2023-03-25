package com.jaehong.presentation.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaehong.domain.model.CenterItem
import com.jaehong.domain.model.result.ApiResult
import com.jaehong.domain.usecase.SplashUseCase
import com.jaehong.presentation.ui.navigation.Destination
import com.jaehong.presentation.ui.navigation.VaccinationAppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashUseCase: SplashUseCase,
    private val vaccinationAppNavigator: VaccinationAppNavigator,
) : ViewModel() {

    private val _centerInfoState = MutableStateFlow(false)
    val centerInfoState = _centerInfoState.asStateFlow()

    private val _animationState = MutableStateFlow(0)
    val animationState = _animationState.asStateFlow()

    private val _loadingValue = MutableStateFlow(0)
    val loadingValue = _loadingValue.asStateFlow()

    private val _networkConnectState = MutableStateFlow(false)
    val networkConnectState = _networkConnectState.asStateFlow()

    private lateinit var loading: Job

    init {
        observeNetworkState()
    }

    fun startLoading() {
        for (idx in 1..10) {
            getCenterItems(idx)
        }

        increaseLoadingValue()

        if (loadingValue.value == 80 && centerInfoState.value.not()) {
            loading.cancel()
        }

        if (loadingValue.value == 100) {
            loading.cancel()
            onNavigateToMapView()
        }
    }

    private fun increaseLoadingValue() {
        loading = viewModelScope.launch {
            delay(20)
            _loadingValue.value++
            _animationState.value += 2
        }
    }

    private fun getCenterItems(page: Int) {
        viewModelScope.launch {
            splashUseCase.getCenterInfo(page)
                .catch { Log.d("Get Center Items", "${it.message}") }
                .collect {
                    when (it) {
                        is ApiResult.Success -> {
                            insertCenterItems(it.data, page)
                        }
                        is ApiResult.Error -> {
                            Log.d("Get Center Items", "${it.exception.message}")
                        }
                    }
                }
        }
    }

    private fun insertCenterItems(items: List<CenterItem>, page: Int) {
        viewModelScope.launch {
            splashUseCase.insertCenterItems(items)
                .catch { Log.d("Insert Center Items", "${it.message}") }
                .collect {
                    if (it && page == 10) {
                        checkedLoadingValue()
                    }
                    if (it.not()) {
                        Log.d("Insert Center Items", "No.$page Insert Failure")
                    }
                }
        }
    }

    private fun checkedLoadingValue() {
        _centerInfoState.value = true
        if (loading.isCancelled && loadingValue.value == 80) {
            increaseLoadingValue()
        }
    }

    private fun onNavigateToMapView() {
       viewModelScope.launch {
           vaccinationAppNavigator.navigateBack()
            vaccinationAppNavigator.navigateTo(Destination.MapView())
        }
    }

    // Network Check
    private fun observeNetworkState() {
        viewModelScope.launch {
            splashUseCase.observeConnectivityAsFlow()
                .catch { Log.d("Get Gudie Key Error", "result: ${it.message}") }
                .collect {
                    _networkConnectState.value = it
                }
        }
    }

    fun initLoadingValue() {
        _animationState.value = 0
        _loadingValue.value = 0
    }
}