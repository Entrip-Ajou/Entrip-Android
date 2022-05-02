package ajou.paran.entrip.screen.planner.top.useradd

//import ajou.paran.entrip.repository.PlannerRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlannerUserAddActivityViewModel
    @Inject
    constructor(
//        private val plannerRepository: PlannerRepository
) : ViewModel() {
    companion object{
        const val TAG = "[PlannerUserAddActivityViewModel]"
    }

}