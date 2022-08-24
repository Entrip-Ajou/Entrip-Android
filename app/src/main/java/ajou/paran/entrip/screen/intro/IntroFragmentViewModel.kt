package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.repository.network.UserRemoteSource
import ajou.paran.entrip.repository.usecase.FindByIdUseCase
import ajou.paran.entrip.repository.usecase.GetUserPlannersUseCase
import ajou.paran.entrip.repository.usecase.IsExistUserUseCase
import ajou.paran.entrip.screen.intro.register.RegisterActivityViewModel
import ajou.paran.entrip.screen.splash.SplashActivity
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroFragmentViewModel
@Inject
constructor(
    private val isExistUserUseCase: IsExistUserUseCase,
    private val getUserPlannersUseCase: GetUserPlannersUseCase,
    private val findByIdUseCase: FindByIdUseCase,
    private val userRemoteSource: UserRemoteSource,
    private val sharedPreferences: SharedPreferences
): ViewModel()
{
    companion object{
        const val TAG = "[IntroFragmentVM]"
    }

    private val _isExistUserResult = MutableStateFlow<ApiState>(ApiState.Init)
    private val _getUserPlannersResult = MutableStateFlow<ApiState>(ApiState.Init)
    private val _findByIdResult = MutableStateFlow<ApiState>(ApiState.Init)

    val isExistUserResult: StateFlow<ApiState>
        get() = _isExistUserResult
    val getUserPlannersResult: StateFlow<ApiState>
        get() = _getUserPlannersResult
    val findByIdResult : StateFlow<ApiState>
        get() = _findByIdResult

//    lateinit var user_id: String

    fun result(user_id: String) = viewModelScope.launch{
        isExistUserUseCase
            .execute(user_id)
            .collect {
                when(it) {
                    is BaseResult.Success -> {
                        if (it.data){
                            _isExistUserResult.value = ApiState.Failure(999)
                        } else {
                            _isExistUserResult.value = ApiState.Success(it)
                        }
                    }
                    is BaseResult.Error -> {
                        _isExistUserResult.value = ApiState.Failure(it.err.code)
                    }
                }
            }
    }

    fun getUserPlanners(user_id: String) = viewModelScope.launch {
        getUserPlannersUseCase
            .execute(user_id)
            .collect {
                when(it) {
                    is BaseResult.Success -> {
                        Log.d(SplashActivity.TAG, "사용자 DB update 완료")
                        _getUserPlannersResult.value = ApiState.Success(it)
                    }
                    is BaseResult.Error -> {
                        Log.e(SplashActivity.TAG, "Err code = ${it.err.code}, Err message = ${it.err.message}")
                        _getUserPlannersResult.value = ApiState.Failure(it.err.code)
                    }
                }
            }
    }

    fun findById(user_id : String) = viewModelScope.launch{
        findByIdUseCase
            .execute(user_id)
            .collect{
                if(it is BaseResult.Success){
                    Log.d(SplashActivity.TAG, "기존 회원이 앱을 삭제했을 경우 서버에 기존 회원 정보를 가져옴")
                    _findByIdResult.value = ApiState.Success(it)
                }else {
                    Log.e(SplashActivity.TAG, "Err code"+(it as BaseResult.Error).err.code+ " Err message = "+it.err.message)
                    _findByIdResult.value = ApiState.Failure(it.err.code)
                }
            }
    }

    fun commitShared(user_id : String, photo_url : String, nickname : String){
        Log.d(TAG, "[sharedPreference] user_id : $user_id")
        Log.d(TAG, "[sharedPreference] photo_url : $photo_url")
        Log.d(TAG, "[sharedPreference] nickname : $nickname")

        sharedPreferences.edit().putString("user_id", user_id).commit()
        sharedPreferences.edit().putString("photo_url", photo_url).commit()
        sharedPreferences.edit().putString("nickname", nickname).commit()

        viewModelScope.launch(Dispatchers.IO){
            when(val res = userRemoteSource.updateUserToken(user_id, sharedPreferences.getString("token", null)!!)) {
                is BaseResult.Success -> { Log.d(TAG, "기존 사용자가 앱을 지운 후 다시 깔았을 경우 Token update 완료") }
                is BaseResult.Error -> { Log.e(RegisterActivityViewModel.TAG, "Err code = ${res.err.code}, Err message = ${res.err.message}") }
            }
        }
    }

    fun isTokenNull() : Boolean{
        return sharedPreferences.getString("token", null).isNullOrEmpty()
    }
}