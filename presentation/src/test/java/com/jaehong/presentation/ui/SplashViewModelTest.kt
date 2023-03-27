package com.jaehong.presentation.ui

import android.util.Log
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jaehong.domain.model.CenterItem
import com.jaehong.domain.model.UiState
import com.jaehong.domain.model.result.ApiResult
import com.jaehong.domain.usecase.SplashUseCase
import com.jaehong.presentation.ui.navigation.VaccinationAppNavigator
import com.jaehong.presentation.ui.splash.SplashViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.DisplayName

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val getSplashUseCase: SplashUseCase = mockk()
    private val navigator: VaccinationAppNavigator = mockk()

    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val viewModel: SplashViewModel by lazy { SplashViewModel(getSplashUseCase, navigator) }

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("[성공] App 시작 시 네트워크가 연결 되어 있을 경우: Loading -> SUCCESS / loading Value 값 0 에서 100 증가 확인")
    fun checkNetworkConnectSuccess() = runTest {
        // given
        coEvery {
            getSplashUseCase.observeConnectivityAsFlow()
        } returns flow { emit(true) }

        for (i in 1..10) {
            coEvery {
                getSplashUseCase.getCenterInfo(i)
            } returns flow { emit(ApiResult.Success(listOf(centerItemTest))) }
        }

        coEvery {
            getSplashUseCase.insertCenterItems(listOf(centerItemTest))
        } returns flow { emit(true) }

        // 초기 값 확인
        assertThat(viewModel.uiState.value).isEqualTo(UiState.LOADING)

        // when
        for (i in 0..99) {
            viewModel.startLoading()
        }

        // then
        viewModel.uiState.test {
            assertThat(this.awaitItem()).isEqualTo(UiState.SUCCESS)
        }

        viewModel.loadingValue.test {
            for (i in 0..100) {
                assertThat(this.awaitItem()).isEqualTo(i)
            }
        }
    }

    @Test
    @DisplayName("[실패] App 시작 시 네트워크가 연결 되어 있지 않을 경우: 함수 실행 X -> 초기값만 확인")
    fun checkNetworkConnectError() = runTest {
        // given
        coEvery {
            getSplashUseCase.observeConnectivityAsFlow()
        } returns flow { emit(false) }

        for (i in 1..10) {
            coEvery {
                getSplashUseCase.getCenterInfo(i)
            } returns flow { emit(ApiResult.Success(listOf(centerItemTest))) }
        }

        coEvery {
            getSplashUseCase.insertCenterItems(listOf(centerItemTest))
        } returns flow { emit(true) }

        // when X

        // then
        viewModel.uiState.test {
            assertThat(this.awaitItem()).isEqualTo(UiState.ERROR)
        }

        viewModel.loadingValue.test {
            assertThat(this.awaitItem()).isEqualTo(0)
        }
    }

    @Test
    @DisplayName("네트워크 데이터 저장이 완료되지 않았을 경우 : UiState: Loading")
    fun checkNetworkConnectSlow() = runTest {
        // given
        coEvery {
            getSplashUseCase.observeConnectivityAsFlow()
        } returns flow { emit(true) }

        for (i in 1..10) {
            coEvery {
                getSplashUseCase.getCenterInfo(i)
            } returns flow { emit(ApiResult.Success(listOf(centerItemTest))) }
        }

        coEvery {
            getSplashUseCase.insertCenterItems(listOf(centerItemTest))
        } returns flow { emit(false) }

        // when
        for (i in 0..99) {
            viewModel.startLoading()
        }

        // then
        viewModel.uiState.test {
            assertThat(this.awaitItem()).isEqualTo(UiState.LOADING)
        }
    }

    companion object {
        val centerItemTest = CenterItem("", "", "", "", "", "", "", "", "")
    }
}