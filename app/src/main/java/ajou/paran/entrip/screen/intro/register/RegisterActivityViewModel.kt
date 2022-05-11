package ajou.paran.entrip.screen.intro.register

import ajou.paran.entrip.repository.usecase.IsExistNicknameUseCase
import ajou.paran.entrip.repository.usecase.IsSaveUserUseCase
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import android.content.SharedPreferences
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val saveUserUseCase: IsSaveUserUseCase
): ViewModel() {
    companion object{
        const val TAG = "[RegisterActivityViewModel]"
    }

    lateinit var user_id: String

    private val _isExistNicknameResult = MutableStateFlow<ApiState>(ApiState.Init)

    val isExistNicknameReuslt: StateFlow<ApiState>
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
                    if (!it.data.userId.isNullOrEmpty()){
                        _isSaveUserResult.value = ApiState.Success(it)
                    } else {
                        _isSaveUserResult.value = ApiState.Failure(999)
                    }
                } else {
                    _isSaveUserResult.value = ApiState.Failure((it as BaseResult.Error).err.code)
                }
            }
    }

    fun userIdShared() = sharedPreferences.edit().putString("user_id", user_id)
}