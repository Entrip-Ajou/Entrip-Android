package com.paran.presentation.views.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInActivityViewModel
@Inject
constructor(

) : ViewModel() {
    companion object {
        private const val TAG = "SignInActVM"
    }

    val inputEmail: MutableLiveData<String> = MutableLiveData("")
    val inputPassword: MutableLiveData<String> = MutableLiveData("")

    fun onClickSignUp(view: View) {
        Log.d(TAG, "email = ${inputEmail.value}, password = ${inputPassword.value}")
    }
}