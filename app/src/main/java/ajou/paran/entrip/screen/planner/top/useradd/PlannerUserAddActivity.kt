package ajou.paran.entrip.screen.planner.top.useradd

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityUseraddBinding
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlannerUserAddActivity: BaseActivity<ActivityUseraddBinding>(
    R.layout.activity_useradd
), View.OnClickListener {
    companion object{
        const val TAG = "[PlannerUserAddActivity]"
    }

    private val viewModel: PlannerUserAddActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {

    }

    override fun onClick(view: View?) {
        view?.let {
            when(it.id){
                /*
                binding.userAddActBtnClose.id -> {
                    onBackPressed()
                }
                 */
            }

        }
    }
}