package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivitySplashBinding
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashActivity: BaseActivity<ActivitySplashBinding>(R.layout.activity_splash){
    companion object{
        const val TAG = "[SplashActivity]"
    }

    override fun init(savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenResumed {
            delay(1500L)
            val pref = getSharedPreferences("checkFirst", Activity.MODE_PRIVATE)
            val checkFirst = pref.getBoolean("checkFirst", false)
            if (!checkFirst){
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, PlannerActivity::class.java))
            }
        }
    }

}