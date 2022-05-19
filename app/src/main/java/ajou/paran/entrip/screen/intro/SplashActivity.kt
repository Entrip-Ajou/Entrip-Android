package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivitySplashBinding
import ajou.paran.entrip.screen.home.HomeActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity: BaseActivity<ActivitySplashBinding>(R.layout.activity_splash){
    companion object{
        const val TAG = "[SplashActivity]"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun init(savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenResumed {
            delay(1500L)
            val checkFirst = sharedPreferences.getString("user_id", null).isNullOrEmpty()
            if (checkFirst){
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            }
        }
    }

}