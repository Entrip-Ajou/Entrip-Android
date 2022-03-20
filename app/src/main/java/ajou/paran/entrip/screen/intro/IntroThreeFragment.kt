package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentIntroThreeBinding
import ajou.paran.entrip.screen.intro.login.LoginActivity
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroThreeFragment: BaseFragment<FragmentIntroThreeBinding>(R.layout.fragment_intro_three) {
    companion object{
        const val TAG = "[IntroThreeFragment]"
    }

    override fun init() {
        binding.introThreeActBtnLogin.setOnClickListener{
            Log.d(TAG, "Case: Click Login")
            startActivity(Intent(activity, LoginActivity::class.java))
        }
        binding.introThreeActBtnNext.setOnClickListener{
            Log.d(TAG, "Case: Click Next")
            startActivity(Intent(activity, PlannerActivity::class.java))
        }
    }
}