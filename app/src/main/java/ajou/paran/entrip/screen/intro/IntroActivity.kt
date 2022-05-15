package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityIntroBinding
import ajou.paran.entrip.screen.intro.register.RegisterActivity
import ajou.paran.entrip.screen.planner.main.MainActivity
import ajou.paran.entrip.util.ApiState
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint

private const val NUM_PAGES = 4

@AndroidEntryPoint
class IntroActivity: BaseActivity<ActivityIntroBinding>(R.layout.activity_intro) {
    companion object{
        const val TAG = "[IntroActivity]"
    }

    override fun init(savedInstanceState: Bundle?) {
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        binding.introActVp2.adapter = pagerAdapter


    }

    override fun onBackPressed() {
        if (binding.introActVp2.currentItem == 0){
            super.onBackPressed()
        }else {
            binding.introActVp2.currentItem -= 1
        }
    }

    private inner class ScreenSlidePagerAdapter(fragmentActivity: FragmentActivity)
        : FragmentStateAdapter(fragmentActivity){

        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment{
            return when(position){
                0 -> IntroOneFragment()
                1 -> IntroTwoFragment()
                2 -> IntroThreeFragment()
                3 -> IntroFourFragment()
                else -> IntroOneFragment()
            }
        }
    }
}