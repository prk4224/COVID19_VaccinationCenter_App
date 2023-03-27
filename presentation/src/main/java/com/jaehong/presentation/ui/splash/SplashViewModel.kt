package com.jaehong.presentation.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaehong.domain.model.CenterItem
import com.jaehong.domain.model.UiState
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

    private val TAG = "SplashViewModel"

    private val _uiState = MutableStateFlow(UiState.EMPTY)
    val uiState = _uiState.asStateFlow()

    private val _loadingWidth = MutableStateFlow(0)
    val loadingWidth = _loadingWidth.asStateFlow()

    private val _loadingValue = MutableStateFlow(0)
    val loadingValue = _loadingValue.asStateFlow()

    private val _networkConnectState = MutableStateFlow(false)
    val networkConnectState = _networkConnectState.asStateFlow()

    private lateinit var loadingScope: Job

    init {
        observeNetworkState()
    }

    fun startLoading() {

        if (loadingValue.value == 80 && uiState.value != UiState.SUCCESS) {
            loadingScope.cancel()
        }

        if (loadingValue.value == 100 && uiState.value == UiState.SUCCESS) {
            loadingScope.cancel()
            onNavigateToMapView()
        }

        if(loadingValue.value < 100) {
            increaseLoadingValue()
        }

        if (loadingValue.value == 0) {
            for (idx in 1..10) {
                getCenterItems(idx)
            }
        }
    }

    private fun increaseLoadingValue() {
        loadingScope = viewModelScope.launch {
            delay(20)
            _loadingValue.value++
            _loadingWidth.value += 2
        }
    }

    private fun getCenterItems(page: Int) {
        viewModelScope.launch {
            splashUseCase.getCenterInfo(page)
                .catch { Log.d(TAG, "Get Center Items: ${it.message}") }
                .collect {
                    when (it) {
                        is ApiResult.Success -> {
                            insertCenterItems(it.data, page)
                        }
                        is ApiResult.Error -> {
                            Log.d(TAG, "Get Center Items: ${it.exception.message}")
                        }
                    }
                }
        }
    }

    private fun insertCenterItems(items: List<CenterItem>, page: Int) {
        viewModelScope.launch {
            splashUseCase.insertCenterItems(items)
                .catch { Log.d(TAG, "Insert Center Items: ${it.message}") }
                .collect {
                    if (it && page == 10) {
                        completeInsertCenterItems()
                    }
                    if (it.not()) {
                        Log.d(TAG, "Insert Center Items: No.$page Insert Failure")
                    }
                }
        }
    }

    private fun completeInsertCenterItems() {
        updateUiState(UiState.SUCCESS)
        if (loadingScope.isCancelled && uiState.value != UiState.SUCCESS) {
            increaseLoadingValue()
        }
    }

    private fun onNavigateToMapView() {
        viewModelScope.launch {
            vaccinationAppNavigator.navigateBack()
            vaccinationAppNavigator.navigateTo(Destination.MapView())
        }
    }

    private fun observeNetworkState() {
        viewModelScope.launch {
            splashUseCase.observeConnectivityAsFlow()
                .catch { Log.d("Get Gudie Key Error", "result: ${it.message}") }
                .collect {
                    _networkConnectState.value = it
                    if (it.not() && uiState.value != UiState.SUCCESS) {
                        updateUiState(UiState.ERROR)
                    } else {
                        updateUiState(UiState.LOADING)
                    }
                }
        }
    }

    fun updateUiState(state: UiState) {
        _uiState.value = state
    }

    fun initLoadingValue() {
        _loadingWidth.value = 0
        _loadingValue.value = 0
    }
}