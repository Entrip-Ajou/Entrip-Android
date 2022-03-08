package ajou.paran.entrip.screen.activity

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityPlannerBinding
import ajou.paran.entrip.model.PlannerDate
import ajou.paran.entrip.model.fakeDateItemList
import ajou.paran.entrip.screen.adapter.DateRecyclerViewAdapter
import ajou.paran.entrip.screen.viewmodel.PlannerActivityViewModel
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PlannerActivity: BaseActivity<ActivityPlannerBinding>(
    R.layout.activity_planner,
    { ActivityPlannerBinding.inflate(it) }
), View.OnClickListener {
    companion object{
        const val TAG = "PlannerActivity"
    }

    private lateinit var dateRecyclerViewAdapter: DateRecyclerViewAdapter
    private val viewModel: PlannerActivityViewModel by viewModels()

    override fun init() {

        // insert onClickListener
        binding.plannerActIvClose.setOnClickListener(this)
//        binding.plannerActTvTitle.setOnClickListener(this)
        binding.plannerActIbTitleEdit.setOnClickListener(this)
        binding.plannerActTvDate.setOnClickListener(this)
        binding.plannerActIvDateEdit.setOnClickListener(this)
        binding.plannerActIvPlannerAdd.setOnClickListener(this)
        binding.plannerActPersonAdd.setOnClickListener(this)

        initDateRecyclerView()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlannerDateItem()
    }

    /**
     * @funcName: onClick
     * @func: View Click Event
     * @Case
     *  - Close
     *  - Click planner title button
     *  - Change planner title text
     *  - Edit planner Date
     *  - Add planner
     *  - Add user
     * @Date: 2022.03.08
     * @Made: Jeon
     * **/
    override fun onClick(view: View?) {
        view?.let {
            when(it.id){
                binding.plannerActIvClose.id -> {
                    Log.d(TAG, "Case: Close")

                }
                binding.plannerActIbTitleEdit.id -> {
                    Log.d(TAG, "Case: Click planner title button")

                    when (binding.plannerActEtTitle.isEnabled) {
                        true -> {
                            binding.plannerActEtTitle.isEnabled = false
                            Log.d(TAG, "Case: Change planner title text")
                            /**
                             * 플래너의 타이틀 변경으로 인하여 내부 db의 값 변경 로직 필요
                             * **/

                        }
                        false -> binding.plannerActEtTitle.isEnabled = true
                    }
                }
                binding.plannerActTvDate.id, binding.plannerActIvDateEdit.id -> {
                    Log.d(TAG, "Case: Edit planner Date")

                    var startDate: PlannerDate
                    var endDate: PlannerDate

                    val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select dates")
                        .setSelection(
                            Pair(
                                MaterialDatePicker.thisMonthInUtcMilliseconds(),
                                MaterialDatePicker.todayInUtcMilliseconds()
                            ))
                        .build()

                    dateRangePicker.addOnPositiveButtonClickListener {
                    val (s_year, s_month, s_day) = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(it.first)
                        .split(".")
                        .map { it.toInt() }
                    val (e_year, e_month, e_day) = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(it.second)
                        .split(".")
                        .map { it.toInt() }

                    val cal = Calendar.getInstance()
                    cal.set(s_year, s_month, s_day)
                    val mutableList = mutableListOf<PlannerDate>()

                    while (cal.get(Calendar.YEAR) != e_year
                        || cal.get(Calendar.MONTH) != e_month
                        || cal.get(Calendar.DAY_OF_MONTH) != e_day) {
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
                    viewModel.setPlannerDateItem(mutableList.toList())
                    binding.plannerActTvDate.text = "$s_month/$s_day ~ $e_month/$e_day"
                    }

                    dateRangePicker.show(supportFragmentManager,"")
                }
                binding.plannerActIvPlannerAdd.id -> {
                    Log.d(TAG, "Case: Add planner")
                }
                binding.plannerActPersonAdd.id -> {
                    Log.d(TAG, "Case: Add user")
                }
                else -> {
                    return
                }
            }
        }
    }

    /**
     * @funcName: initDateRecyclerView
     * @func: Recycler Setting(Adapter, LayoutManager)
     * @Date: 2022.03.08
     * @Made: Jeon
     * **/
    private fun initDateRecyclerView() = binding.plannerActRv1.apply {
        dateRecyclerViewAdapter = DateRecyclerViewAdapter()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = dateRecyclerViewAdapter
    }

    /**
     * @funcName: subscribeObservers
     * @func: observe
     * @Date: 2022.03.08
     * @Made: Jeon
     * **/
    private fun subscribeObservers() {
        viewModel.plannerDateItemList.observe(this, Observer { dateRecyclerViewAdapter.submitList(it) })
    }

}