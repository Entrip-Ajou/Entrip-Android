package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.repository.Impl.UserV2Repository
import ajou.paran.entrip.repository.Impl.UserV2RepositoryImpl
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
    private val userV2Repository: UserV2Repository
) : ViewModel() {
    companion object{
        private const val TAG = "[IntroFragmentVM]"
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
        when (userV2Repository.loginUserAccount(userId = userId, password = password)) {
            true -> { _loginState.postValue(LoginState.Success(isSuccess = true))   }
            false -> { _loginState.postValue(LoginState.Error(isSuccess = false))  }
        }
    }

}