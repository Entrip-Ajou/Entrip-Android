package com.paran.presentation.views.viewmodel

import ajou.paran.domain.usecase.IsExistNicknameUseCase
import ajou.paran.domain.usecase.IsExistUserUseCase
import ajou.paran.domain.usecase.SaveUserAccountUseCase
import android.util.Log
import android.view.View
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
    private val isExistNicknameUseCase: IsExistNicknameUseCase,
    private val saveUserAccountUseCase: SaveUserAccountUseCase,
) : ViewModel() {
    companion object {
        private const val TAG = "SignUpActVM"
    }
    val inputUserId = MutableLiveData("")
    val inputPassword = MutableLiveData("")
    val inputPasswordCheck = MutableLiveData("")
    val inputNickname = MutableLiveData("")

    private val _isNotExistUser = MutableLiveData(false)
    val isNotExistUser: LiveData<Boolean>
        get() = _isNotExistUser

    private val _isNotExistNickname = MutableLiveData(false)
    val isNotExistNickname: LiveData<Boolean>
        get() = _isNotExistNickname

    private val _gender = MutableLiveData(0)
    val gender: LiveData<Int>
        get() = _gender

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun checkIsExistUserByUserId() = inputUserId.value?.let {
        when(it.checkEmailVerify()) {
            true -> {
                CoroutineScope(Dispatchers.IO).launch {
                    when(isExistUserUseCase(params = IsExistUserUseCase.Params(userId = it))) {
                        true -> {
                            _isNotExistUser.postValue(false)
                            _errorMessage.postValue("이미 존재하는 아이디입니다.")
                        }
                        false -> {
                            _isNotExistUser.postValue(true)
                        }
                    }
                }
            }
            false -> {
                _errorMessage.postValue("아이디를 다시 한번 확인해주세요.")
            }
        }
    }

    fun checkIsExistUserByNickname() = inputNickname.value?.let {
        when (it.checkNicknameVerify()) {
            true -> {
                CoroutineScope(Dispatchers.IO).launch {
                    when(isExistNicknameUseCase(params = IsExistNicknameUseCase.Params(nickname = it))) {
                        true -> {
                            _isNotExistNickname.postValue(false)
                            _errorMessage.postValue("이미 존재하는 닉네임입니다.")
                        }
                        false -> {
                            _isNotExistNickname.postValue(true)
                        }
                    }
                }
            }
            false -> {
                _errorMessage.postValue("닉네임을 다시 확인해주세요.")
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }

    fun onClickGender(gender: Int) {
        _gender.value = gender
    }

    fun onClickSignUp() {
        when (checkSignUpCondition()) {
            true -> {
                CoroutineScope(Dispatchers.IO).launch {
                    saveUserAccountUseCase(
                        params = SaveUserAccountUseCase.Params(
                            userId = inputUserId.value ?: "",
                            nickname = inputNickname.value ?: "",
                            photoUrl = "",
                            gender = gender.value?.toLong() ?: 0L,
                            password = inputPassword.value ?: ""
                        )
                    ).onSuccess {

                    }.onFailure {
                        _errorMessage.postValue("회원가입에 실패했습니다.")
                    }
                }
            }
            false -> _errorMessage.value = "회원가입 정보를 다시 한번 살펴주세요."
        }
    }

    private fun String.checkEmailVerify() = Regex("^[\\w\\.-]+@([\\da-zA-Z-]+\\.)+[a-zA-Z]{2,6}\$").matches(this)

    private fun String.checkNicknameVerify() = this.length in 4..12

    private fun checkSignUpCondition(): Boolean = (
        inputUserId.value?.isNotEmpty() ?: false
            && _isNotExistUser.value ?: false
            && inputPassword.value?.isNotEmpty() ?: false
            && inputPasswordCheck.value?.isNotEmpty() ?: false
            && (inputPassword.value ?: "") == (inputPasswordCheck.value ?: "")
            && inputNickname.value?.isNotEmpty() ?: false
            && _isNotExistNickname.value ?: false
    )

}