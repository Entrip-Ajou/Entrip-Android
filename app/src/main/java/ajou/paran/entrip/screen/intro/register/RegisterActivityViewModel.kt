package ajou.paran.entrip.screen.intro.register

import ajou.paran.entrip.repository.Impl.UserRepository
import ajou.paran.entrip.repository.network.UserRemoteSource
import ajou.paran.entrip.screen.intro.LoginState
import ajou.paran.entrip.screen.intro.RegisterState
import ajou.paran.entrip.util.network.BaseResult
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterActivityViewModel
@Inject
constructor(
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository,
    private val userRemoteSource: UserRemoteSource
): ViewModel() {
    companion object{
        const val TAG = "[RegisterViewModel]"
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
    val isCheckNickname: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val gender: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>(-1L)
    }

    fun duplicationCheck() {
        if (nickname.value!!.toString().isNotEmpty())
            checkNickname(nickname = nickname.value!!.toString())
    }

    fun repeatedAttempt() {
        isCheckNickname.value = false
    }

    private fun checkNickname(nickname: String) = viewModelScope.launch(Dispatchers.IO) {
        when (userRepository.isExistNickname(nickname)) {
            true -> {
                isCheckNickname.postValue(false)
            }
            false -> {
                isCheckNickname.postValue(true)
            }
        }
    }

    fun register() {
        _registerState.value = RegisterState.Loading

        if (userId.value!!.toString().isNotEmpty()
            && password.value!!.toString().isNotEmpty()
            && nickname.value!!.toString().isNotEmpty()
            && gender.value!! != -1L
        ) {
            saveUser(
                userId = userId.value.toString(),
                nickname = nickname.value.toString(),
                password =  password.value.toString(),
                gender = gender.value!!
            )
        } else {
            _registerState.value = RegisterState.Error(isSuccess = false)
        }
    }

    private fun saveUser(
        userId: String,
        nickname: String,
        password: String,
        gender: Long
    ) = viewModelScope.launch(Dispatchers.IO) {
        val response = userRepository.saveUserAccount(
            userId = userId,
            nickname = nickname,
            photoUrl = "",
            token = "",
            gender = gender,
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
        when (userRepository.loginUserAccount(userId = userId, password = password)) {
            true -> {
                _loginState.postValue(LoginState.Success(isSuccess = true))
                sharedPreferences.edit().putString("nickname", nickname.value).commit()
                sharedPreferences.edit().putString("gender", gender.toString()).commit()
                sharedPreferences.edit().putString("photo_url","https://user-images.githubusercontent.com/77181865/169517449-f000a59d-5659-4957-9cb4-c6e5d3f4b197.png").commit()
            }
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
        gender.value == -1L -> {
            "성별을 선택해주세요"
        }
        else -> {
            "네트워크를 확인해주세요"
        }
    }

    fun updateUserToken(){
        sharedPreferences.getString("user_id", null)?.let { userID ->
            sharedPreferences.getString("token", null)?.let { userToken ->
                viewModelScope.launch(Dispatchers.IO) {
                    when (val res = userRemoteSource.updateUserToken(userID, userToken)) {
                        is BaseResult.Success -> { Log.d(TAG, "초기 사용자가 등록할 때 Token 등록 완료") }
                        is BaseResult.Error -> { Log.e(TAG, "Err code = ${res.err.code}, Err message = ${res.err.message}") }
                    }
                }
            }
        }
    }
}