package ajou.paran.entrip.screen.splash

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivitySplashBinding
import ajou.paran.entrip.screen.home.HomeActivity
import ajou.paran.entrip.screen.intro.IntroActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    companion object {
        const val TAG = "[SplashActivity]"
    }

    private val viewModel: SplashActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        check()
    }

    private fun check() = lifecycleScope.launchWhenResumed {
        if (viewModel.userID.isEmpty()) {
            delay(1000L)
            val intent = Intent(this@SplashActivity, IntroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            this@SplashActivity.finish()
            startActivity(intent)
        } else {
            val isPatchToken = async { viewModel.patchToken() }
            if (isPatchToken.await()) {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                this@SplashActivity.finish()
                startActivity(intent)
            } else {
                val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                this@SplashActivity.finish()
                startActivity(intent)
            }
        }
    }
}