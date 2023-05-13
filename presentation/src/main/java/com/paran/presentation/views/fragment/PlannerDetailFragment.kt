package com.paran.presentation.views.fragment

import ajou.paran.domain.model.BasePlan
import ajou.paran.domain.model.BasePlanner
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.paran.presentation.R
import com.paran.presentation.common.adapter.PlanAdapter
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.databinding.FragmentPlannerDetailBinding
import com.paran.presentation.utils.callbacks.SwipeHelperCallback
import com.paran.presentation.views.activity.HomeActivity
import com.paran.presentation.views.viewmodel.PlannerDetailFragmentViewModel

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
    private val planAdapter = PlanAdapter(
        onClickDeletePlan = this::onClickDelete,
        onClickPlan = this::onClickPlan,
        onClickComment = this::onClickComment
    )

    override fun init() {
        binding.fragment = this
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
        subObserver()
        initAdapter()
    }

    private fun subObserver() {

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initAdapter() {
        val swipeHelperCallback = SwipeHelperCallback(planAdapter).apply {
            setClamp(resources.displayMetrics.widthPixels.toFloat()/4)
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.rvPlan)

        binding.rvPlan.setOnTouchListener { view, motionEvent ->
            swipeHelperCallback.removePreviousClamp(binding.rvPlan)
            false
        }

        binding.rvPlan.adapter = planAdapter
    }

    private fun onClickDelete(plan: BasePlan) {

    }

    private fun onClickPlan(plan: BasePlan) {

    }

    private fun onClickComment(plan: BasePlan) {

    }

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