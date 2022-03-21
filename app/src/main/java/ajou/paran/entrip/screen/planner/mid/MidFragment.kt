package ajou.paran.entrip.screen.planner.mid

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentMidBinding
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MidFragment: BaseFragment<FragmentMidBinding>(R.layout.fragment_mid) {
    companion object{
        private const val TAG = "[MidFragment]"
    }

    override fun init() {
        Log.d(TAG, "생성")
    }
}