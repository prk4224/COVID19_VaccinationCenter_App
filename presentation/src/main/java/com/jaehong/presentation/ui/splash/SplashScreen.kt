package com.jaehong.presentation.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jaehong.domain.model.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val uiState = splashViewModel.uiState.collectAsState().value
    val networkConnectState = splashViewModel.networkConnectState.collectAsState().value
    val loadingValue = splashViewModel.loadingValue.collectAsState().value
    val loadingWidth = splashViewModel.loadingWidth.collectAsState().value

    if(uiState == UiState.EMPTY) {
        if(networkConnectState.not()) splashViewModel.updateUiState(UiState.ERROR)
        Box(modifier = Modifier.fillMaxSize())
    }

    if(uiState == UiState.ERROR) {
        NetworkErrorScreen {
            splashViewModel.initLoadingValue()
        }
    }

    if(uiState == UiState.LOADING || uiState == UiState.SUCCESS){
        LoadingScreen(
            loadingValue = loadingValue,
            loadingWidth = loadingWidth,
            startLoading = {
                splashViewModel.startLoading()
            }
        )
    }
}