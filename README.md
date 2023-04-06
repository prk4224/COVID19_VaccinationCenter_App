# COVID19_VaccinationCenter_App (코로나 19 예방접종센터 지도 App)

## 실행 방법
API Key, Client Id 를 Local Properties에서 관리
- local.properties 에 밑 정보 입력 후 실행가능

```
api_key = "bNmSjmL3NWL/mAmsQV0SyDT+8DCdZckhVg5/tSsmJHa47eBZBE+aFvCHYxeM1Dsz2FcgQ64elqYL3mr6GUyjOg=="
client_id = "hx1egfkmv4"
```
### ❗️실행 안될 시 
- API Key: Data Module → util 패키지 → Constants → API_KEY 에 직접 추가
- Client Id: App Module → Manifest → meta-data value 에 client_id 직접 추가

---

## 구현 모습
|<img src="https://user-images.githubusercontent.com/83493143/227767768-96117326-8494-404b-8208-38df7ffde07d.gif" width="250" />|<img src="https://user-images.githubusercontent.com/83493143/227767778-a40efa90-f5dd-4bb0-8851-5c0555b5bc81.gif" width="250"/>|<img src="https://user-images.githubusercontent.com/83493143/227767793-2bc121de-b2ac-44b5-85f4-768007184c70.gif" width="250" />|
|:--:|:--:|:--:|
|정상 Splash Screen|네트워크 연결 X Splash Screen|네크워크 느린 Splash Screen|

|<img src="https://user-images.githubusercontent.com/83493143/227767808-df33f0ba-58d8-4e93-b516-8d65dbebf95c.gif" width="250" />|<img src="https://user-images.githubusercontent.com/83493143/227767515-780a312c-0443-46ea-a9d5-320fcaa62902.gif" width="250"/>|<img src="https://user-images.githubusercontent.com/83493143/227767567-61ed0f80-0b84-4064-90ce-b3451dff52a6.gif" width="250" />|
|:--:|:--:|:--:|
|위치 권한이 없을 때 초기 화면|위치 권한 없을 때 현재 위치 버튼 클릭|마커 이벤트|

---

## ⚒ 구조

### 개발 환경
- Language : Kotlin
- minSdk : 21
- targetSdk : 32
- compileSdk 33

### 사용한 라이브러리
- JetPack Compose
- JetPack Compose Navigation
- Retrofit2 + OkHttp3
- Coroutine + Flow
- Hlit
- Room
- Never Map Compose 
: Naver Map 을 Compose 에  지원해 주는 라이브러리 (https://github.com/fornewid/naver-map-compose)
- Mockk + Truth + turbine + TestCoroutine

### 📌 멀티 모듈 & MVVM
#### 클린아키텍쳐 적용
<img width="956" alt="스크린샷 2023-02-07 오후 7 32 35" src="https://user-images.githubusercontent.com/83493143/227753590-fba5f914-fb59-4ba6-a890-51682fd1ed29.png">

---

## 💡 고민 사항
### 📌 지도에 Marker 가 표시되는 시점
: 지도가 로딩 될때 모든 마커가 표시 되므로 비효율적이라 판단.

#### ❗️ 해결 방법
<img width="855" alt="스크린샷 2023-03-26 오후 1 37 05" src="https://user-images.githubusercontent.com/83493143/227755563-0550e498-033b-4586-9e95-bce4306305a1.png">

#### 거리 계산 함수
```kotlin
private fun getDistance(center: LatLng, target: LatLng): Double {
    val earthRadius = 6372.8 * 1000
    val diffLat = Math.toRadians(center.latitude - target.latitude)
    val diffLon = Math.toRadians(center.longitude - target.longitude)
    val a = kotlin.math.sin(diffLat / 2).pow(2.0)+
            kotlin.math.sin(diffLon / 2).pow(2.0) *
            kotlin.math.cos(Math.toRadians(target.latitude)) *
            kotlin.math.cos(Math.toRadians(center.latitude))
    val c = 2 * kotlin.math.asin(kotlin.math.sqrt(a))
    return earthRadius * c
}
```

#### 범위 체크 함수
```kotlin
fun checkedRangeForMarker(
    center: LatLng,
    rangeLocation: LatLng?,
    targetLocation: LatLng
): Boolean {
    val range = getDistance(center,rangeLocation?: return false)
    val distance = getDistance(center,targetLocation)

    return range > distance
}
```

#### Naver Map 에서 구현 내용
```kotlin
NaverMap(
    properties = mapProperties,
    uiSettings = mapUiSettings,
    cameraPositionState = cameraPositionState,
    onMapLoaded = { initPosition() },
    onMapClick = { point, latLng -> onMapClick(point,latLng) }
) {
    centerItems.forEach {
        if(checkedRangeForMarker(
                cameraPositionState.position.target, // 지도의 중앙 위치 좌표
                cameraPositionState.contentBounds?.northEast, // 지도의 북동쪽 위치 좌표
                LatLng(it.lat.toDouble(),it.lng.toDouble())) // 마커 좌표 
        ) {
            marker(it)
        }
    }
}
```

---

### 📌 로딩 바

#### 요구 사항
- 2초에 걸쳐 100%가 되도록 로딩바 구현 
- 단, API 데이터 저장이 완료되지 않았다면 80%에서 대기  (저장이 완료되면 0.4초 걸쳐 100%를 만든 후 Map 화면으로 이동)

#### Loading 상태를 관리하는 Job 객체 정의
```kotlin
private lateinit var loadingScope: Job
```

#### UI State 구현
```kotlin
private val _uiState = MutableStateFlow(UiState.EMPTY)
val uiState = _uiState.asStateFlow()
```

#### ❗️ 2초에 100% 로딩 → 0.02초당 1% 로딩

```kotlin
private fun increaseLoadingValue() {
    loadingScope = viewModelScope.launch {
        delay(20)
        _loadingValue.value++
        _loadingWidth.value += 2
    }
}
```

#### 로딩 시작 함수 구현
```kotlin
fun startLoading() {

    // 증가 Scope 실행
    if(loadingValue.value < 100) {
        increaseLoadingValue()
    }

    // 80% 에서 상태 체크 로딩이 완료되지 않았다면 증가 Scoope cancle
    if (loadingValue.value == 80 && uiState.value != UiState.SUCCESS) {
        loadingScope.cancel()
    }
    // 100% 에 데이터 저장 성공 시 Scoope cancle 후 Map Page 로 이동
    if (loadingValue.value == 100 && uiState.value == UiState.SUCCESS) {
        loadingScope.cancel()
        onNavigateToMapView()
    }

    // 데이터 불러오기
    if (loadingValue.value == 0) {
        for (idx in 1..10) {
            getCenterItems(idx)
        }
    }
}
```

#### 저장 완료 시 Loading 상태 체크
```kotlin
private fun insertCenterItems(items: List<CenterItem>, page: Int) {
    viewModelScope.launch {
        splashUseCase.insertCenterItems(items)
            .catch { Log.d(TAG, "Insert Center Items: ${it.message}") }
            .collect {
                  // 마지막 페이지면 Loading 상태 체크 호출
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
      // Loading 상태를 성공으로 만든다
      updateUiState(UiState.SUCCESS)
      // 로딩 완료후 증가 Scoope 가 cancle 이면서 Loading Value 가 80% 라면 증가 Scoope를 다시 실행 시킨다.
      if (loadingScope.isCancelled && uiState.value != UiState.SUCCESS) {
          increaseLoadingValue()
      }
}
```
---

### 📌 Network 상태 체크
: Network 가 연결되어 있지 않다면 Loading 80% 에서 움직이지 않는 문제

#### Network Manager Data Module 구현
```kotlin
interface NetworkManager {

    fun getConnectivityManager(): ConnectivityManager

    fun getNetworkRequest(): NetworkRequest
}
```

####  Network Manager 정의 후 App Module 에서 의존성 주입

```kotlin
class NetworkManagerImpl(private val context: Context): NetworkManager {
    override fun getConnectivityManager(): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    override fun getNetworkRequest(): NetworkRequest {
        return NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
    }
}
```

```kotlin
@Module
@InstallIn(SingletonComponent::class)
class NetworkManagerModule {

    @Singleton
    @Provides
    fun provideNetworkManager(
        @ApplicationContext context: Context
    ): NetworkManager = NetworkManagerImpl(context)
}
```

#### NetworkConnect CallBak 함수 구현 (연결 되어 있으면 true, 아니면 false)
```kotlin
fun networkConnectCallback(callback: (Boolean) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(true)
        }

        override fun onLost(network: Network) {
            callback(false)
        }
    }
}
```

#### Network 상태 ovserve 함수 구현
```kotlin
override suspend fun observeConnectivityAsFlow():Flow<Boolean> = callbackFlow {
    val connectivityManager = networkManager.getConnectivityManager()
    val callback = networkConnectCallback { result -> trySend(result) }
    val networkRequest = networkManager.getNetworkRequest()

    connectivityManager.registerNetworkCallback(networkRequest,callback)

    awaitClose {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}
```

#### Network State 구현
```kotlin
private val _networkConnectState = MutableStateFlow(false)
val networkConnectState = _networkConnectState.asStateFlow()
```

#### Network 가 연결 되어있지 않을 때 연결 UI 표출
```kotlin
// NetWork 가 연결되어 있지 않고 로딩도 완료되지 않았다면 연결 요청 UI 표출
if(networkConnectState.not() && loadingState.not()) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "네트워크를 연결해 주세요 !", color = Color.Red, fontSize = 20.sp)
    }
    // 로딩이 된 후에 네트워크가 끊겼을 경우 다시 처름부터 loading 되기 때문에 Loding Value 값을 초기화 한다.
    splashViewModel.initLoadingValue()
} 
```

---

## 💡 Unit Test

### Test용 Dispatcher 및 Mockk 객체 생성

- Dispatcher를 Main으로 초기화
- 사용할 UseCase mockk 객체로 생성
- Log mokk 객체로 생성

```kotlin
class SplashViewModelTest {

    private val getSplashUseCase: SplashUseCase = mockk()
    private val navigator: VaccinationAppNavigator = mockk()

    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val viewModel: SplashViewModel by lazy { SplashViewModel(getSplashUseCase,navigator) }

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
}
```

### 네트워크 연결 O Test

```kotlin
@Test
@DisplayName("[성공] App 시작 시 네트워크가 연결 되어 있을 경우: Loading -> SUCCESS / loading Value 값 0 에서 100 증가 확인")
fun checkNetworkConnectSuccess() = runTest {
    // given
    coEvery {
        getSplashUseCase.observeConnectivityAsFlow()
    } returns flow { emit(true) }

    for(i in 1 ..10) {
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
    for(i in 0 .. 99) {
        viewModel.startLoading()
    }

    // then
    viewModel.uiState.test {
        assertThat(this.awaitItem()).isEqualTo(UiState.SUCCESS)
    }

    viewModel.loadingValue.test {
        for(i in 0 .. 100) {
            assertThat(this.awaitItem()).isEqualTo(i)
        }
    }
}
```

### 네트워크 연결 X Test
```kotlin
@Test
@DisplayName("[실패] App 시작 시 네트워크가 연결 되어 있지 않을 경우: 함수 실행 X -> 초기값만 확인")
fun checkNetworkConnectError() = runTest {
    // given
    coEvery {
        getSplashUseCase.observeConnectivityAsFlow()
    } returns flow { emit(false) }

    for(i in 1 ..10) {
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
```

### 데이터 저장 실패 Test
```kotlin
@Test
@DisplayName("네트워크 데이터 저장이 완료되지 않았을 경우 : when 실행후에 UI State -> Loading")
fun checkNetworkConnectSlow() = runTest {
    // given
    coEvery {
        getSplashUseCase.observeConnectivityAsFlow()
    } returns flow { emit(true) }

    for(i in 1 ..10) {
        coEvery {
            getSplashUseCase.getCenterInfo(i)
        } returns flow { emit(ApiResult.Success(listOf(centerItemTest))) }
    }

    coEvery {
        getSplashUseCase.insertCenterItems(listOf(centerItemTest))
    } returns flow { emit(false) }

    // when
    for(i in 0 .. 99) {
        viewModel.startLoading()
    }

    // then
    viewModel.uiState.test {
        assertThat(this.awaitItem()).isEqualTo(UiState.LOADING)
    }
} 
```



