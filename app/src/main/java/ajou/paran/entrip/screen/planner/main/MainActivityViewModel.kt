package ajou.paran.entrip.screen.planner.main

import ajou.paran.entrip.repository.Impl.PlannerRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject
constructor(private val plannerRepository: PlannerRepository)
    : ViewModel() {

    fun createPlanner() = CoroutineScope(Dispatchers.IO).launch {
        plannerRepository.createPlanner("test1")
    }
}