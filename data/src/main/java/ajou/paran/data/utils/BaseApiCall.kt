package ajou.paran.data.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

private const val UnknownErrorMessage: String = "Unknown Error"

suspend inline fun <T> baseApiCall(
    crossinline function: suspend () -> T,
): T = try {
    withContext(Dispatchers.IO) {
        function.invoke()
    }
} catch (e: HttpException) {
    throw when (e.code()) {
        else -> {
            throw Exception()
        }
    }
} catch (e: Exception) {
    Log.d("BaseApi", e.message ?: "")
    throw Exception()
}

//fun getErrorMessage(exception: HttpException): ErrorResponse {
//    val errorString = exception.response()?.errorBody()?.string()
//    val errorDto: ErrorResponse = Gson().fromJson(
//        errorString, ErrorResponse::class.java
//    )
//    Log.d("TAG", "errorString: ${errorDto.message}, errorCode:${errorDto.errorCode}")
//    return errorDto
//}

suspend inline fun <T> reIssueApiCall(
    crossinline function: suspend () -> T,
): T = try {
    withContext(Dispatchers.IO) {
        function.invoke()
    }
} catch (e: Exception) {
    Log.d("BaseApi", e.message ?: "")
    throw Exception(e.message)
}