package ajou.paran.entrip.screen.splash

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivitySplashBinding
import ajou.paran.entrip.repository.network.UserRemoteSource
import ajou.paran.entrip.repository.usecase.GetUserPlannersUseCase
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.screen.home.HomeActivity
import ajou.paran.entrip.screen.intro.IntroActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_planner.*
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity: BaseActivity<ActivitySplashBinding>(R.layout.activity_splash){
    companion object{
        const val TAG = "[SplashActivity]"
    }

    private val viewModel: SplashActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        check()
    }

    private fun check() = lifecycleScope.launchWhenResumed {
        if(viewModel.userID.isEmpty()){
            delay(1000L)
            startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
        } else {
            if(viewModel.token.isEmpty()) {
                patchPlanner()
            } else {
                val isPatchToken = async { viewModel.patchToken() }
                val isPatchPlanner = async { viewModel.patchPlannerSub() }
                if(isPatchToken.await() && isPatchPlanner.await()){
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                }
            }
        }
    }

    private suspend fun patchPlanner()
    = viewModel.getUserPlannersUseCase.execute(viewModel.userID)
        .collect {
            when(it) {
                is BaseResult.Success -> {
                    Log.d(TAG, "사용자 DB update 완료")

                    withContext(Dispatchers.Main){
                        startActivity(
                            Intent(
                                this@SplashActivity,
                                HomeActivity::class.java
                            )
                        )
                    }
                }
                is BaseResult.Error -> {
                    Log.e(TAG, "Err code = ${it.err.code}/ Err message = ${it.err.message}")

                    withContext(Dispatchers.Main) {
                        startActivity(
                            Intent(
                                this@SplashActivity,
                                IntroActivity::class.java
                            )
                        )
                    }
                }
            }
        }



}