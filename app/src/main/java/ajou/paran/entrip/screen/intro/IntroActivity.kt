package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityIntroBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
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
        when(binding.introActVp2.currentItem) {
            0 -> {
                binding.introActVp2.currentItem = 0
            }
            else -> {
                binding.introActVp2.currentItem -= 1
            }
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