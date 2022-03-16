package ajou.paran.entrip.screen.planner.top

import ajou.paran.entrip.model.PlannerDate
import ajou.paran.entrip.repository.PlannerRepository
import android.annotation.SuppressLint
import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
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
        private const val TAG = "[PlannerActivityViewModel]"
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

    @SuppressLint("RestrictedApi")
    fun startDateRangePicker(supportFragmentManager: FragmentManager) {
        val selector = CustomMaterialDatePicker()
        val dateRangePicker = MaterialDatePicker.Builder.customDatePicker(selector)
            .setTitleText("Select dates")
            .setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .build()
        dateRangePicker.show(supportFragmentManager,"Hello")

        dateRangePicker.addOnPositiveButtonClickListener {
            val (s_year, s_month, s_day) = SimpleDateFormat(
                "yyyy.MM.dd",
                Locale.getDefault()
            ).format(it.first)
                .split(".")
                .map { it.toInt() }
            val (e_year, e_month, e_day) = SimpleDateFormat(
                "yyyy.MM.dd",
                Locale.getDefault()
            ).format(it.second)
                .split(".")
                .map { it.toInt() }

            val cal = Calendar.getInstance()
            cal.set(s_year, s_month, s_day)
            val mutableList = mutableListOf<PlannerDate>()

            while (cal.get(Calendar.YEAR) != e_year
                || cal.get(Calendar.MONTH) != e_month
                || cal.get(Calendar.DAY_OF_MONTH) != e_day
            ) {
                // 플래너에 집어 넣을 Date 생성 로직
                mutableList.add(
                    PlannerDate(
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    )
                )
                cal.add(Calendar.DAY_OF_MONTH, 1)
            }
            mutableList.add(PlannerDate(e_year, e_month, e_day))
            setPlannerDateItem(mutableList.toList())
            mutableList.clear()
        }
    }
}