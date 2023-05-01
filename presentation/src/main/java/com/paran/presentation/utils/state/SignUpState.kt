package com.paran.presentation.utils.state

sealed class SignUpState {
    object Init : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
}