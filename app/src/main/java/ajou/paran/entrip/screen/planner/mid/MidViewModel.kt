package ajou.paran.entrip.screen.planner.mid

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.repository.room.plan.repository.PlanRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MidViewModel @Inject constructor(
    private val planRepository: PlanRepository
    ): ViewModel() {

    companion object{
        private const val TAG = "[MidViewModel]"
    }

    private val _items = MutableLiveData<List<PlanEntity>>()
    val items : LiveData<List<PlanEntity>> = _items

    init {
        loadPlan()
    }

    private fun loadPlan(){
        viewModelScope.launch{
            val plans = planRepository.selectPlan()
            _items.value = plans!!
        }
    }

}