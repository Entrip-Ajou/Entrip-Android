package com.paran.presentation.views.viewmodel

import ajou.paran.domain.usecase.IsExistUserUseCase
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpActivityViewModel
@Inject
constructor(
    private val isExistUserUseCase: IsExistUserUseCase,
) : ViewModel() {
    companion object {
        private const val TAG = "SignUpActVM"
    }
    val inputUserId = MutableLiveData("")

    private val _isNotExistUser = MutableLiveData(false)
    val isNotExistUser: LiveData<Boolean>
        get() = _isNotExistUser

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun checkIsExistUserByUserId() = inputUserId.value?.let {
        when(it.checkEmailVerify()) {
            true -> {
                CoroutineScope(Dispatchers.IO).launch {
                    isExistUserUseCase(
                        params = IsExistUserUseCase.Params(
                            userId = it
                        )
                    ).onSuccess {
                        _isNotExistUser.postValue(true)
                    }.onFailure {
                        _isNotExistUser.postValue(false)
                        _errorMessage.postValue("이미 존재하는 아이디입니다.")
                    }
                }
            }
            false -> {
                _errorMessage.postValue("아이디를 다시 한번 확인해주세요.")
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }

    private fun String.checkEmailVerify() = Regex("^[\\w\\.-]+@([\\da-zA-Z-]+\\.)+[a-zA-Z]{2,6}\$").matches(this)

}