package com.paran.presentation.views.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETActivity
import com.paran.presentation.databinding.ActivitySplashBinding
import com.paran.presentation.views.viewmodel.SplashActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "SplashAct"
    }

    private val splashViewModel: SplashActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testSplash()
    }

    private fun testSplash() = CoroutineScope(Dispatchers.IO).launch {
        delay(1500)
        startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
        finish()
    }

}