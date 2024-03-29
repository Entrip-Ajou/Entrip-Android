package com.paran.presentation.views.viewmodel

import ajou.paran.domain.usecase.FindAllPlannersByUserUseCase
import ajou.paran.domain.usecase.SignInByUserAccountUseCase
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paran.presentation.utils.state.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInActivityViewModel
@Inject
constructor(
    private val signInByUserAccountUseCase: SignInByUserAccountUseCase,
    private val findAllPlannersByUserUseCase: FindAllPlannersByUserUseCase
) : ViewModel() {
    companion object {
        private const val TAG = "SignInActVM"
    }

    val inputEmail: MutableLiveData<String> = MutableLiveData("")
    val inputPassword: MutableLiveData<String> = MutableLiveData("")

    private val _signInState = MutableLiveData<Boolean>(false)
    val signInState: LiveData<Boolean>
        get() = _signInState

    private val _routeState = MutableLiveData<SignInState>(SignInState.Init)
    val routeState: LiveData<SignInState>
        get() = _routeState

    fun onClickSignUp(view: View) {
        Log.d(TAG, "email = ${inputEmail.value}, password = ${inputPassword.value}")
    }

    fun onClickSignIn() {
        _routeState.value = SignInState.Loading
        when (checkSignInCondition()) {
            true -> {
                CoroutineScope(Dispatchers.IO).launch {
                    signInByUserAccountUseCase(
                        params = SignInByUserAccountUseCase.Params(
                            userId = inputEmail.value ?: "",
                            password = inputPassword.value ?: "",
                        )
                    ).onSuccess {
                        _signInState.postValue(true)
                    }.onFailure {
                        _routeState.postValue(SignInState.Init)
                        _signInState.postValue(false)
                    }
                }
            }
            false -> {
                _routeState.value = SignInState.Init
                _signInState.postValue(false)
            }
        }
    }

    private fun checkSignInCondition(): Boolean = (
        inputEmail.value?.isNotEmpty() ?: false
            && inputPassword.value?.isNotEmpty() ?: false
    )

    fun syncDBToServer() = CoroutineScope(Dispatchers.IO).launch {
        findAllPlannersByUserUseCase(inputEmail.value ?: "")
            .onSuccess { _routeState.postValue(SignInState.Success) }
            .onFailure { _routeState.postValue(SignInState.Success) }
    }

}