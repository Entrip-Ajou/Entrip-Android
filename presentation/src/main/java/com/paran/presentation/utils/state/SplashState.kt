package com.paran.presentation.utils.state

sealed class SplashState {
    object Init : SplashState()
    object Intro : SplashState()
    object SignIn : SplashState()
    object Home : SplashState()
}
