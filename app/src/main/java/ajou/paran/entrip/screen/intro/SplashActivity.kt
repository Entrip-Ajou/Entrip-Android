package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivitySplashBinding
import ajou.paran.entrip.repository.network.UserRemoteSource
import ajou.paran.entrip.repository.usecase.GetUserPlannersUseCase
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.screen.home.HomeActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity: BaseActivity<ActivitySplashBinding>(R.layout.activity_splash){
    companion object{
        const val TAG = "[SplashActivity]"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var userRemoteSource: UserRemoteSource

    @Inject
    lateinit var getUserPlannersUseCase: GetUserPlannersUseCase

    override fun init(savedInstanceState: Bundle?) {
        check()
    }

    private fun check() = lifecycleScope.launchWhenResumed {
        val userID = sharedPreferences.getString("user_id", null)
        if(userID.isNullOrEmpty()){
            delay(1000L)
            startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
        } else {
            val token = sharedPreferences.getString("token", null)
            if(token.isNullOrEmpty()) {
                patchPlanner(userID)
            } else {
                val isPatchToken = async { patchToken(userID, token) }
                val isPatchPlanner = async { patchPlannerSub(userID) }
                if(isPatchToken.await() && isPatchPlanner.await()){
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                }
            }
        }
    }

    private suspend fun patchToken(userID: String, token: String): Boolean
    = when(val res = userRemoteSource.updateUserToken(userID, token)) {
        is BaseResult.Success -> {
            Log.d(TAG, "사용자 Token update 완료")
            true
        }
        is BaseResult.Error -> {
            Log.e(TAG, "Err code = ${res.err.code}/ Err message = ${res.err.message}")
            false
        }
    }

    private suspend fun patchPlanner(userID: String)
    = getUserPlannersUseCase.execute(userID)
        .collect {
            when(it) {
                is BaseResult.Success -> {
                    Log.d(TAG, "사용자 DB update 완료")

                    withContext(Dispatchers.Main){
                        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    }
                }
                is BaseResult.Error -> {
                    Log.e(TAG, "Err code = ${it.err.code}/ Err message = ${it.err.message}")

                    withContext(Dispatchers.Main) {
                        startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                    }
                }
            }
        }

    private suspend fun patchPlannerSub(userID: String) : Boolean
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