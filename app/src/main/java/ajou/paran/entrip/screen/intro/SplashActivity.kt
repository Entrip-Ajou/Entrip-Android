package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivitySplashBinding
import ajou.paran.entrip.repository.network.UserRemoteSource
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

    override fun init(savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenResumed {
            delay(1500L)
            val checkFirst = sharedPreferences.getString("user_id", null).isNullOrEmpty()
            if (checkFirst){
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            } else {
                val user_id = sharedPreferences.getString("user_id", null)?.toString()
                val token = sharedPreferences.getString("token", null)?.toString()
                if(!token.isNullOrEmpty()){
                    lifecycleScope.launch(Dispatchers.IO) {
                        val res = userRemoteSource.updateUserToken(user_id!!,token!!)
                        if(res is BaseResult.Success){
                            Log.d(TAG, "사용자 Token update 완료")
                        }else{
                            Log.e(TAG, "Err code = "+(res as BaseResult.Error).err.code+ " Err message = "+res.err.message)
                        }
                        withContext(Dispatchers.Main){
                            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                        }
                    }
                }
            }
        }
    }
}