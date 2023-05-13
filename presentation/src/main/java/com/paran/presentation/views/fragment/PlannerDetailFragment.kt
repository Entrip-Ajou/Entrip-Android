package com.paran.presentation.views.fragment

import ajou.paran.domain.model.BasePlan
import ajou.paran.domain.model.BasePlanner
import ajou.paran.domain.model.PlannerDate
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.paran.presentation.R
import com.paran.presentation.common.adapter.PlanAdapter
import com.paran.presentation.common.adapter.PlannerDateAdapter
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.databinding.FragmentPlannerDetailBinding
import com.paran.presentation.utils.callbacks.SwipeHelperCallback
import com.paran.presentation.views.activity.HomeActivity
import com.paran.presentation.views.viewmodel.PlannerDetailFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PlannerDetailFragment : BaseETFragment<FragmentPlannerDetailBinding>(R.layout.fragment_planner_detail) {
    companion object {
        fun newInstance(planner: BasePlanner): PlannerDetailFragment = PlannerDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable("planner", planner)
            }
        }

        private const val TAG = "PlannerDetailFrag"
    }

    private val viewModel: PlannerDetailFragmentViewModel by viewModels()
    private val dateAdapter = PlannerDateAdapter(onClick = this::onClickDate)
    private val planAdapter = PlanAdapter(
        onClickDeletePlan = this::onClickDelete,
        onClickPlan = this::onClickPlan,
        onClickComment = this::onClickComment
    )
    private val swipeHelperCallback = SwipeHelperCallback(planAdapter)

    override fun init() {
        binding.fragment = this
        subObserver()
        initDateAdapter()
        initPlanAdapter()

        kotlin.runCatching {
            viewModel.postPlanner(
                when {
                    Build.VERSION.SDK_INT >= 33 -> arguments?.getParcelable("planner", BasePlanner::class.java)!!
                    else -> @Suppress("DEPRECATION") arguments?.getParcelable("planner")!!
                }
            )
        }.onFailure {
            (requireActivity() as HomeActivity)
        }
    }

    private fun subObserver() {
        viewModel.selectedPlanner.observe(this) {
            binding.plannerTvStartDate.text = it.startDate
            binding.plannerActTvEndDate.text = it.endDate
            dateAdapter.submitList(getDates(it.startDate, it.endDate))
        }
        viewModel.loadPlanList.observe(this) {
            swipeHelperCallback.removeMyClamp(binding.rvPlan)
            planAdapter.submitList(it)
        }
        viewModel.selectedDate.observe(this) {
            viewModel.loadPlanData(it)
        }
    }

    private fun initDateAdapter() {
        binding.plannerRvDate.adapter = dateAdapter
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initPlanAdapter() {
        swipeHelperCallback.setClamp(resources.displayMetrics.widthPixels.toFloat()/4)
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.rvPlan)

        binding.rvPlan.setOnTouchListener { view, motionEvent ->
            swipeHelperCallback.removePreviousClamp(binding.rvPlan)
            false
        }

        Log.d(TAG, "Init PlanAdapter")
        binding.rvPlan.adapter = planAdapter
    }

    private fun onClickDelete(plan: BasePlan) {

    }

    private fun onClickPlan(plan: BasePlan) {

    }

    private fun onClickComment(plan: BasePlan) {

    }

    private fun onClickDate(date: PlannerDate) {
        viewModel.loadPlanData(date.date)
    }

    private fun getDates(
        startDate: String,
        endDate: String
    ): MutableList<PlannerDate> = kotlin.runCatching {
        val format = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

        val (startYear, startMonth, startDay) = startDate.split("/").map { it.toInt() }
        val time = Calendar.getInstance().apply { set(startYear, startMonth, startDay) }

        val t = kotlin.math.abs(format.parse(startDate)!!.time - format.parse(endDate)!!.time)
        val dates = t / (24 * 60 * 60 * 1000)

        var i = -1
        val mutableList = mutableListOf<PlannerDate>()
        time.add(Calendar.MONTH, -1)

        while (i < dates){
            mutableList.add(
                PlannerDate(format.format(time.time))
            )
            time.add(Calendar.DAY_OF_MONTH, 1)
            i += 1
        }

        return@runCatching mutableList
    }.getOrDefault(mutableListOf())

    fun hideKeyboard(view: View, event: MotionEvent): Boolean {
        val inputManager: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        requireActivity().currentFocus?.let {
            inputManager.hideSoftInputFromWindow(
                it.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
        return false
    }

    fun onBackPress() {
        when (val activity = requireActivity()) {
            is HomeActivity -> {
                activity.cleanPlannerData()
            }
        }
    }

}