package ajou.paran.entrip.screen.community

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentCommunityBinding
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment : BaseFragment<FragmentCommunityBinding>(R.layout.fragment_community){
    companion object{
        const val TAG = "[CommunityFragment]"
    }

    private val viewModel: CommunityFragmentViewModel by viewModels()

    override fun init() {

    }

}