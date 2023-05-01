package com.paran.presentation.utils.state

sealed class IntroState {
    object Init : IntroState()
    object SignIn : IntroState()
}
