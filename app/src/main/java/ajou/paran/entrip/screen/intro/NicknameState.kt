package ajou.paran.entrip.screen.intro

sealed class NicknameState {
    data class Success(
        val isSuccess: Boolean,
    ) : NicknameState()

    data class Error(
        val isSuccess: Boolean,
        val reason: String = ""
    ) : NicknameState() {
        companion object {
            const val EMPTY = "empty"
            const val EXIST = "EXIST"
        }
    }

    object Init : NicknameState()

    object Loading : NicknameState()
}
