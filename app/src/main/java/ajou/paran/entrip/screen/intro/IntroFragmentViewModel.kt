package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.repository.Impl.UserRepository
import ajou.paran.entrip.util.network.BaseResult
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroFragmentViewModel
@Inject
constructor(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    companion object{
        const val TAG = "[IntroFragmentVM]"
    }

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState>
        get() = _loginState

    val userId: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }
    val userPassword: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    fun loginUserAccount() {
        Log.d(TAG, "click login")
        _loginState.value = LoginState.Loading
        if (userId.value!!.isEmpty() && userPassword.value!!.isEmpty()){
            Log.d(TAG, "Empty data")
            _loginState.value = LoginState.Error(isSuccess = false)
        } else {
            Log.d(TAG, "run login")
            loginNetworking(
                userId = userId.value!!,
                password = userPassword.value!!
            )
        }
    }

    private fun loginNetworking(
        userId: String,
        password: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        when (userRepository.loginUserAccount(userId = userId, password = password)) {
            is BaseResult.Success -> { _loginState.postValue(LoginState.Success(isSuccess = true)) }
            is BaseResult.Error -> { _loginState.postValue(LoginState.Error(isSuccess = false))  }
        }
    }

    fun isTokenNull() : Boolean =  sharedPreferences.getString("token", null).isNullOrEmpty()
}