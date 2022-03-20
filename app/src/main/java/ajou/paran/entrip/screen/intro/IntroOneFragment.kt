package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentIntroOneBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroOneFragment: BaseFragment<FragmentIntroOneBinding>(R.layout.fragment_intro_one) {
    companion object{
        const val TAG = "[IntroOneFragment]"
    }

    override fun init() {

    }

}