package ajou.paran.entrip.screen.intro

sealed class RegisterState {
    data class Success(
        val isSuccess: Boolean,
        val userId: String,
        val userPassword: String
    ) : RegisterState()

    data class Error(
        val isSuccess: Boolean,
        val reason: String = ""
    ) : RegisterState() {
        companion object {
            const val EXIST = "existAccount"
        }
    }

    object Loading : RegisterState()
}