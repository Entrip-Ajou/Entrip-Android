package ajou.paran.entrip.util


sealed class ApiState {
    object Init : ApiState()
    data class IsLoading(val isLoading : Boolean) : ApiState()
    data class Success(val data : Any) : ApiState()
    data class Failure(val code : Int) : ApiState()
}
