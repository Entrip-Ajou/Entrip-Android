package ajou.paran.entrip.screen.intro.register

import ajou.paran.entrip.repository.network.UserRemoteSource
import ajou.paran.entrip.repository.usecase.IsExistNicknameUseCase
import ajou.paran.entrip.repository.usecase.IsSaveUserUseCase
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.fcm.MyFirebaseMessaingService
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterActivityViewModel
@Inject
constructor(
    private val sharedPreferences: SharedPreferences,
    private val isExistNicknameUseCase: IsExistNicknameUseCase,
    private val saveUserUseCase: IsSaveUserUseCase,
    private val userRemoteSource: UserRemoteSource
): ViewModel() {
    companion object{
        const val TAG = "[RegisterViewModel]"
    }

    lateinit var user_id: String

    private val _isExistNicknameResult = MutableStateFlow<ApiState>(ApiState.Init)

    val isExistNicknameResult: StateFlow<ApiState>
        get() = _isExistNicknameResult

    private val _isSaveUserResult = MutableStateFlow<ApiState>(ApiState.Init)

    val isSaveUserResult: StateFlow<ApiState>
        get() = _isSaveUserResult

    fun nickNameResult(userNickname: String) = viewModelScope.launch {
        isExistNicknameUseCase
            .execute(userNickname)
            .collect {
                if (it is BaseResult.Success){
                    if (it.data){
                        _isExistNicknameResult.value = ApiState.Failure(999)
                    } else {
                        _isExistNicknameResult.value = ApiState.Success(it)
                    }
                } else {
                    _isExistNicknameResult.value = ApiState.Failure((it as BaseResult.Error).err.code)
                }
            }
    }

    fun saveUserResult(gender: Int, nickname: String) = viewModelScope.launch {
        saveUserUseCase
            .execute(user_id, gender, nickname)
            .collect {
                if (it is BaseResult.Success){
                    if (it.data.userId.isNotBlank()){
                        sharedPreferences.edit().putString("nickname", nickname).commit()
                        sharedPreferences.edit().putString("gender", gender.toString()).commit()
                        sharedPreferences.edit().putString("photo_url","https://user-images.githubusercontent.com/77181865/169517449-f000a59d-5659-4957-9cb4-c6e5d3f4b197.png").commit()
                        _isSaveUserResult.value = ApiState.Success(it)
                    } else {
                        _isSaveUserResult.value = ApiState.Failure(999)
                    }
                } else {
                    _isSaveUserResult.value = ApiState.Failure((it as BaseResult.Error).err.code)
                }
            }
    }

    fun userIdShared() = sharedPreferences.edit().putString("user_id", user_id).commit()

    fun updateUserToken(){
        val user_id = sharedPreferences.getString("user_id", null)?.toString()
        val user_token = sharedPreferences.getString("token", null)?.toString()

        viewModelScope.launch(Dispatchers.IO) {
            val res = userRemoteSource.updateUserToken(user_id!!,user_token!!)
            if(res is BaseResult.Success){
                Log.d(TAG, "초기 사용자가 등록할 때 Token 등록 완료")
            }else{
                Log.e(TAG, "Err code = "+(res as BaseResult.Error).err.code+ " Err message = "+res.err.message)
            }
        }
    }
}