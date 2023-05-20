package com.paran.presentation.utils.state

sealed class SignInState {
    object Init : SignInState()
    object Loading : SignInState()
    object Success : SignInState()
}