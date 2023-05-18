package com.paran.presentation.views.fragment

import ajou.paran.domain.model.BasePlan
import ajou.paran.domain.model.BasePlanner
import ajou.paran.domain.model.PlannerDate
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.core.util.Pair
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.datepicker.MaterialDatePicker
import com.paran.presentation.R
import com.paran.presentation.common.adapter.PlanAdapter
import com.paran.presentation.common.adapter.PlannerDateAdapter
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.common.route.HomeRoute
import com.paran.presentation.common.widgets.CustomMaterialDatePicker
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
        onClickComment = this::onClickComment,
        onClickAdd = this::onClickAdd
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

    private fun onClickAdd() {
        when (val activity = requireActivity()) {
            is HomeActivity -> {
                activity.pushRoute(HomeRoute.PlanInput.tag)
            }
        }
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

    @SuppressLint("RestrictedApi")
    fun onClickModifyDate() {
        viewModel.selectedPlanner.value?.let { planner ->
            val selector = CustomMaterialDatePicker()

            val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")

            val formattedStartDate = formatter.parse(planner.startDate)
            val formattedEndDate = formatter.parse(planner.endDate)

            val dateRangePicker = MaterialDatePicker.Builder.customDatePicker(selector)
                .setTitleText("Select dates")
                .setSelection(Pair(formattedStartDate?.time, formattedEndDate?.time))
                .build()

            dateRangePicker.show(this.parentFragmentManager,"Hello")

            dateRangePicker.addOnPositiveButtonClickListener { pairDate ->
                val format = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                val (startYear, startMonth, startDay) = format.format(pairDate.first)
                    .split("/")
                    .map { it.toInt() }
                val (endYear, endMonth, endDay) = format.format(pairDate.second)
                    .split("/")
                    .map { it.toInt() }

                val list = getDates(
                    startDate = "$startYear/$startMonth/$startDay",
                    endDate = "$endYear/$endMonth/$endDay"
                )



                viewModel.modifyDate(format.format(pairDate.first), format.format(pairDate.second))
//                init_start_date = "$s_year/$s_month/$s_day"
//                init_end_date = "$endYear/$e_month/$e_day"
//                midFragment.setAdapter(format.format(pairDate.first))
            }
        }
    }

    fun onClickMenu() {
        val popup = PopupMenu(context, binding.plannerActMenu)
        requireActivity().menuInflater.inflate(R.menu.navigation_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.gallery -> {

                }
                R.id.notice -> (activity as HomeActivity).pushRoute(HomeRoute.Notice.tag)
                R.id.vote -> (activity as HomeActivity).pushRoute(HomeRoute.Vote.tag)
                R.id.invite -> (activity as HomeActivity).pushRoute(HomeRoute.PlannerUserAdd.tag)
            }
            false
        }
        popup.show()
    }
}