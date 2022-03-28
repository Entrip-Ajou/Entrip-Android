package ajou.paran.entrip.screen.planner.top

import ajou.paran.entrip.model.PlannerDate
import ajou.paran.entrip.repository.PlannerRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @ClassName: PlannerActivityViewModel
 * @innerFunc: setPlannerDateItem(List<PlannerDate>), getPlannerDateItem()
 * @private: _plannerDateItemList: MutableLiveData<List<PlannerDate>>
 * **/
@HiltViewModel
class PlannerActivityViewModel
@Inject
constructor(
    private val plannerRepository: PlannerRepository
) : ViewModel() {
    companion object{
        private const val TAG = "[PlannerActViewModel]"
    }

    private val _plannerDateItemList: MutableLiveData<List<PlannerDate>> = MutableLiveData()

    val plannerDateItemList: LiveData<List<PlannerDate>>
        get() = _plannerDateItemList

    fun setPlannerDateItem(list: List<PlannerDate>){
        _plannerDateItemList.value = list
    }

    fun getPlannerDateItem() = viewModelScope.launch {
        val dateItemLiveData = plannerRepository.getPlannerDates()
        withContext(Main){
            _plannerDateItemList.value = dateItemLiveData
        }
    }

    fun plannerDataDelete(){
        TODO("Case: Delete Planner Data")
    }

    fun plannerAdd(){
        TODO("Case: Add Planner")
    }

}