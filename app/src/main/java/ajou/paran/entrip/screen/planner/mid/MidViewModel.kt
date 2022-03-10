package ajou.paran.entrip.screen.planner.mid

import ajou.paran.entrip.repository.room.plan.repository.PlanRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MidViewModel @Inject constructor(
    private val planRepository: PlanRepository
    ): ViewModel() {

    companion object{
        private const val TAG = "[MidViewModel]"
    }

}