package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.repository.network.UserRemoteSource
import ajou.paran.entrip.repository.usecase.FindByIdUseCase
import ajou.paran.entrip.repository.usecase.GetUserPlannersUseCase
import ajou.paran.entrip.repository.usecase.IsExistUserUseCase
import ajou.paran.entrip.screen.home.HomeActivity
import ajou.paran.entrip.screen.intro.register.RegisterActivityViewModel
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                if (it is BaseResult.Success){
                    if (it.data){
                        _isExistUserResult.value = ApiState.Failure(999)
                    } else {
                        _isExistUserResult.value = ApiState.Success(it)
                    }
                } else {
                    _isExistUserResult.value = ApiState.Failure((it as BaseResult.Error).err.code)
                }
            }
    }

    fun getUserPlanners(user_id: String) = viewModelScope.launch {
        getUserPlannersUseCase
            .execute(user_id)
            .collect {
                if (it is BaseResult.Success) {
                    Log.d(SplashActivity.TAG, "????????? DB update ??????")
                    _getUserPlannersResult.value = ApiState.Success(it)
                } else {
                    Log.e(SplashActivity.TAG, "Err code = "+(it as BaseResult.Error).err.code+ " Err message = "+it.err.message)
                    _getUserPlannersResult.value = ApiState.Failure(it.err.code)
                }
            }
    }

    fun findById(user_id : String) = viewModelScope.launch{
        findByIdUseCase
            .execute(user_id)
            .collect{
                if(it is BaseResult.Success){
                    Log.d(SplashActivity.TAG, "?????? ????????? ?????? ???????????? ?????? ????????? ?????? ?????? ????????? ?????????")
                    _findByIdResult.value = ApiState.Success(it)
                }else {
                    Log.e(SplashActivity.TAG, "Err code"+(it as BaseResult.Error).err.code+ " Err message = "+it.err.message)
                    _findByIdResult.value = ApiState.Failure(it.err.code)
                }
            }
    }

    fun commitShared(user_id : String, photo_url : String, nickname : String){
        Log.d(TAG, "[sharedPreference] user_id : " + user_id)
        Log.d(TAG, "[sharedPreference] photo_url : " + photo_url)
        Log.d(TAG, "[sharedPreference] nickname : " + nickname)

        sharedPreferences.edit().putString("user_id", user_id).commit()
        sharedPreferences.edit().putString("photo_url", photo_url).commit()
        sharedPreferences.edit().putString("nickname", nickname).commit()

        viewModelScope.launch(Dispatchers.IO){
            val res = userRemoteSource.updateUserToken(user_id, sharedPreferences.getString("token", null)!!)
            if(res is BaseResult.Success){
                Log.d(TAG, "?????? ???????????? ?????? ?????? ??? ?????? ????????? ?????? Token update ??????")
            }else{
                Log.e(RegisterActivityViewModel.TAG, "Err code = "+(res as BaseResult.Error).err.code+ " Err message = "+res.err.message)
            }
        }
    }
}