package ajou.paran.entrip.screen.planner.mid.input

sealed class InputState{

    // 초기상태 -> 아무것도 기입하지 않은 상태
    object initialized : InputState()

    // todo를 기입하지 않은 상태
    object NoTodo : InputState()

    // Time을 기입하지 않은 상태
    object NoTime : InputState()

    // 필수 기입 항목들을 모두 기입한 상태
    object Success : InputState()

    data class Failure(val code : Int) : InputState()

    data class IsLoading(val isLoading : Boolean) : InputState()
}
