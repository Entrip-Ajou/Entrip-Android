package ajou.paran.entrip.screen.splash

import ajou.paran.entrip.repository.network.UserRemoteSource
import ajou.paran.entrip.repository.usecase.GetUserPlannersUseCase
import ajou.paran.entrip.util.network.BaseResult
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel
@Inject
constructor(
    val sharedPreferences: SharedPreferences,
    val userRemoteSource: UserRemoteSource,
    val getUserPlannersUseCase: GetUserPlannersUseCase
) : ViewModel() {
    companion object {
        const val TAG = "[SplashActivityVM]"
    }

    val userID: String
    get() = sharedPreferences.getString("user_id", null) ?: ""

    val token: String
    get() = sharedPreferences.getString("token", null) ?: ""

    suspend fun patchToken(): Boolean
    = when(val res = userRemoteSource.updateUserToken(userID, token)) {
        is BaseResult.Success -> {
            Log.d(SplashActivity.TAG, "사용자 Token update 완료")
            true
        }
        is BaseResult.Error -> {
            Log.e(SplashActivity.TAG, "Err code = ${res.err.code}/ Err message = ${res.err.message}")
            false
        }
    }

    suspend fun patchPlannerSub() : Boolean
    = when(val res = getUserPlannersUseCase.executed(userID)) {
        is BaseResult.Success -> {
            Log.d(TAG, "사용자 DB update 완료")
            true
        }
        is BaseResult.Error -> {
            Log.e(TAG, "Err code = ${res.err.code}/ Err message = ${res.err.message}")
            false
        }
    }

}