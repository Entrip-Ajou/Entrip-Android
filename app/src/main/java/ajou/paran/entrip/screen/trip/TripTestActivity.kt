package ajou.paran.entrip.screen.trip

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityTriptestBinding
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripTestActivity: BaseActivity<ActivityTriptestBinding>(R.layout.activity_triptest) {
    companion object{
        const val TAG = "[TripTestActivity]"
    }

    private val viewModel: TripTestActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {

    }
}