package ajou.paran.entrip.screen.planner.main

import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.room.planner.repository.PlannerRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
    @Inject
    constructor(
        private val plannerRepository: PlannerRepository
    ): ViewModel(){

    fun insert() = viewModelScope.launch {
        plannerRepository.insertPlanner(
            plannerEntity = PlannerEntity(
                title = "제목 없음",
                start_date = "2022/04/01",
                end_date = "2022/04/14",
                time_stamp = Calendar.getInstance().time.toString()
            )
        )
    }
}