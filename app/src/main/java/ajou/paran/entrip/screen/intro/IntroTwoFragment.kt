package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentIntroTwoBinding
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroTwoFragment: BaseFragment<FragmentIntroTwoBinding>(R.layout.fragment_intro_two) {
    companion object{
        const val TAG = "[IntroTwoFragment]"
    }

    override fun init() {
        CoroutineScope(Dispatchers.Main).launch {
            val animation = TranslateAnimation(-500f, 0f, 0f, 0f)
            animation.duration = 1000
            animation.setAnimationListener(object: Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {
                }
                override fun onAnimationEnd(p0: Animation?) {
                    binding.introTwoActText1.visibility = View.VISIBLE
                    binding.introTwoActText2.visibility = View.VISIBLE
                }
                override fun onAnimationRepeat(p0: Animation?) {}
            })
            binding.introTwoActPlanner.startAnimation(animation)
        }
    }

}