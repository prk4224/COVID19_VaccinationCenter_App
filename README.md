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

### 📌 멀티 모듈 & MVVM
#### 클린아키텍쳐 적용
<img width="956" alt="스크린샷 2023-02-07 오후 7 32 35" src="https://user-images.githubusercontent.com/83493143/227753590-fba5f914-fb59-4ba6-a890-51682fd1ed29.png">


## 💡 고민 사항

### 📌 로딩 바

#### 요구 사항
- 2초에 걸쳐 100%가 되도록 로딩바 구현 
- 단, API 데이터 저장이 완료되지 않았다면 80%에서 대기  (저장이 완료되면 0.4초 걸쳐 100%를 만든 후 Map 화면으로 이동)

#### Loading 상태를 관리하는 Job 객체 정의
```kotlin
  private lateinit var loading: Job
```

#### Loading 에 사용할 데이터 구현
```kotlin
  private val _loadingWidth = MutableStateFlow(0)
  val loadingWidth = _loadingWidth.asStateFlow()

  private val _loadingValue = MutableStateFlow(0)
  val loadingValue = _loadingValue.asStateFlow()
```

#### ❗️ 2초에 100% 로딩 → 0.02초당 1% 로딩

```kotlin
  private fun increaseLoadingValue() {
      loading = viewModelScope.launch {
          delay(20)
          _loadingValue.value++
          _loadingWidth.value += 2
      }
  }
```

#### 로딩 시작 함수 구현
```kotlin
  fun startLoading() {
      // 데이터 불러오기
      for (idx in 1..10) {
          getCenterItems(idx)
      }
      // 증가 Scope 실행
      increaseLoadingValue()
      
      // 80% 에서 상태 체크 로딩이 완료되지 않았다면 증가 Scoope cancle
      if (loadingValue.value == 80 && centerInfoState.value.not()) {
          loading.cancel()
      }
      
      // 100% 라면 Scoope cancle 후 Map Page 로 이동
      if (loadingValue.value == 100) {
          loading.cancel()
          onNavigateToMapView()
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
                  if (page == 10) {                
                      checkedLoadingValue()
                  }
                  if (it.not()) {
                      Log.d(TAG, "Insert Center Items: No.$page Insert Failure")
                  }
              }
      }
  }
  private fun checkedLoadingValue() {
      // Loading 상태를 true 로 만든다
      _centerInfoState.value = true
      // 로딩 완료후 증가 Scoope 가 cancle 이면서 Loading Value 가 80% 라면 증가 Scoope를 다시 실행 시킨다.
      if (loading.isCancelled && loadingValue.value == 80) {
          increaseLoadingValue()
      }
  }
```

### 📌 Network 상태 체크

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