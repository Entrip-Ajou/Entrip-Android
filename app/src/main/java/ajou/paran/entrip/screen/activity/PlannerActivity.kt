package ajou.paran.entrip.screen.activity

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityMainBinding
import ajou.paran.entrip.databinding.ActivityPlannerBinding
import ajou.paran.entrip.model.PlannerDate
import ajou.paran.entrip.model.fakeDateItemList
import ajou.paran.entrip.screen.adapter.DateRecyclerViewAdapter
import ajou.paran.entrip.screen.viewmodel.PlannerActivityViewModel
import ajou.paran.entrip.util.getCurrentPosition
import ajou.paran.entrip.util.hideKeyboard
import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.util.Pair
import androidx.core.util.component1
import androidx.core.util.component2
import androidx.databinding.DataBindingUtil
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
        binding.plannerActEtTitle.setOnClickListener(this)
//        binding.plannerActIbTitleEdit.setOnClickListener(this) // 타이틀 에딧 버튼 부분
        binding.plannerActEtTitle.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // 타이틀 변경 이후 키보드 엔터 키를 입력하여 종료시 실행되는 부분
                Log.d(TAG, "Click Enter")
                binding.plannerActEtTitle.inputType = InputType.TYPE_NULL
                hideKeyboard()
                true
            } else {
                false
            }
        }
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
                    onBackPressed()
                }
                binding.plannerActEtTitle.id -> {
                    Log.d(TAG, "Case: Click planner title button")
                    binding.plannerActEtTitle.inputType = InputType.TYPE_CLASS_TEXT
                    /*
                    when (binding.plannerActEtTitle.isEnabled) {
                        true -> {
                            binding.plannerActEtTitle.isEnabled = false
                            Log.d(TAG, "Case: Change planner title text")
                            binding.plannerActIbTitleEdit.setBackgroundResource(R.drawable.ic_baseline_edit_24)
                        }
                        false -> {
                            binding.plannerActEtTitle.isEnabled = true
                            binding.plannerActIbTitleEdit.setBackgroundResource(R.drawable.ic_baseline_check_24)
                            /**
                             * 플래너의 타이틀 변경으로 인하여 내부 db의 값 변경 로직 필요
                             * **/
                        }
                    }
                    */
                }
                binding.plannerActTvDate.id, binding.plannerActIvDateEdit.id -> {
                    Log.d(TAG, "Case: Edit planner Date")
                    viewModel.startDateRangePicker(supportFragmentManager)
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
        viewModel.plannerDateItemList.observe(this, Observer {
            dateRecyclerViewAdapter.submitList(it)
            binding.plannerActTvDate.text = "${it.first().month}/${it.first().day} ~ ${it.last().month}/${it.last().day}"
        })
    }

}