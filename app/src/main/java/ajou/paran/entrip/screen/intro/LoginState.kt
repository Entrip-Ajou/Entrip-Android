package ajou.paran.entrip.screen.intro

sealed class LoginState {
    data class Success(
        val isSuccess: Boolean,
    ) : LoginState()

    data class Error(
        val isSuccess: Boolean,
    ) : LoginState()

    object Loading : LoginState()
}
