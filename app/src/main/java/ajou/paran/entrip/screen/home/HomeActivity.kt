package ajou.paran.entrip.screen.home

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityHomeBinding
import android.content.SharedPreferences
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity: BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    companion object{
        const val TAG = "[HomeActivity]"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun init(savedInstanceState: Bundle?) {

    }

}