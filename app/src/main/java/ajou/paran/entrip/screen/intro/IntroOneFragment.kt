package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentIntroOneBinding
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroOneFragment: BaseFragment<FragmentIntroOneBinding>(R.layout.fragment_intro_one) {
    companion object {
        private const val TAG = "[IntroOneFragment]"
    }

    override fun init() {
        animating()
    }

    override fun onResume() {
        super.onResume()
        animating()
    }

    private fun animating() = CoroutineScope(Dispatchers.Main).launch {
        binding.introOneText.visibility = View.INVISIBLE
        val animation = TranslateAnimation(0f, 0f, 500f, 0f)
        animation.duration = 500
        animation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
                binding.introOneText.visibility = View.VISIBLE
            }
            override fun onAnimationEnd(p0: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
        })
        binding.introOneText.startAnimation(animation)
    }

}