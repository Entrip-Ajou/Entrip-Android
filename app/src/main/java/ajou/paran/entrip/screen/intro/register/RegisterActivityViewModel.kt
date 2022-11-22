package ajou.paran.entrip.screen.intro.register

import ajou.paran.entrip.repository.Impl.UserV2Repository
import ajou.paran.entrip.repository.network.UserRemoteSource
import ajou.paran.entrip.repository.usecase.IsExistNicknameUseCase
import ajou.paran.entrip.repository.usecase.IsSaveUserUseCase
import ajou.paran.entrip.screen.intro.IntroFragmentViewModel
import ajou.paran.entrip.screen.intro.LoginState
import ajou.paran.entrip.screen.intro.RegisterState
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
    private val userV2Repository: UserV2Repository
): ViewModel() {
    companion object{
        private const val TAG = "[RegisterVM]"
    }

    private val _registerState = MutableLiveData<RegisterState>()
    val registerState: LiveData<RegisterState>
        get() = _registerState

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState>
        get() = _loginState

    val userId: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }
    val password: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }
    val nickname: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    fun duplicationCheck() {
        TODO("Check duplication Logic")
    }

    fun register() {
        _registerState.value = RegisterState.Loading

        if (userId.value!!.toString().isNotEmpty()
            && password.value!!.toString().isNotEmpty()
            && nickname.value!!.toString().isNotEmpty()
        ) {
            saveUser(
                userId = userId.value.toString(),
                nickname = nickname.value.toString(),
                password =  password.value.toString()
            )
        } else {
            _registerState.value = RegisterState.Error(isSuccess = false)
        }
    }

    private fun saveUser(
        userId: String,
        nickname: String,
        password: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        val response = userV2Repository.saveUserAccount(
            userId = userId,
            nickname = nickname,
            photoUrl = "",
            token = "",
            gender = 1L,
            password = password
        )
        when (response.userId) {
            userId -> {
                _registerState.postValue(
                    RegisterState.Success(
                        isSuccess = true,
                        userId = userId,
                        userPassword = password
                    )
                )
            }
            else -> {
                _registerState.postValue(RegisterState.Error(isSuccess = false))
            }
        }
    }

    fun loginUserAccount(
        userId: String,
        password: String
    ) {
        _loginState.value = LoginState.Loading

        Log.d(TAG, "run login")
        loginNetworking(
            userId = userId,
            password = password
        )
    }

    private fun loginNetworking(
        userId: String,
        password: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        when (userV2Repository.loginUserAccount(userId = userId, password = password)) {
            true -> { _loginState.postValue(LoginState.Success(isSuccess = true)) }
            false -> { _loginState.postValue(LoginState.Error(isSuccess = false)) }
        }
    }

    fun registerErrorCheck(): String = when {
        userId.value.isNullOrEmpty() -> {
            "ID를 확인해주세요"
        }
        password.value.isNullOrEmpty() -> {
            "Password를 확인해주세요"
        }
        nickname.value.isNullOrEmpty() -> {
            "닉네임을 확인해주세요"
        }
        else -> {
            "네트워크를 확인해주세요"
        }
    }

}