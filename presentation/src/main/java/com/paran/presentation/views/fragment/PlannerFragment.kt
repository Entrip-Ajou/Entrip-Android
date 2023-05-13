package com.paran.presentation.views.fragment

import ajou.paran.domain.model.BasePlanner
import androidx.fragment.app.viewModels
import com.paran.presentation.R
import com.paran.presentation.common.adapter.PlannerAdapter
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.databinding.FragmentPlannerBinding
import com.paran.presentation.views.activity.HomeActivity
import com.paran.presentation.views.viewmodel.PlannerFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlannerFragment : BaseETFragment<FragmentPlannerBinding>(R.layout.fragment_planner) {
    companion object {
        fun newInstance(): PlannerFragment = PlannerFragment().apply {

        }

        private const val TAG = "PlannerFrag"
    }

    private val viewModel: PlannerFragmentViewModel by viewModels()
    private val plannerAdapter: PlannerAdapter = PlannerAdapter(
        onClickPlanner = this::onClickPlanner,
        onClickAdd = this::onClickAdd
    )

    override fun init() {
        subObserver()
        initRecyclerView()
    }

    private fun subObserver() {
        viewModel.plannerList.observe(this) { list ->
            plannerAdapter.submitList(list)
        }
    }

    private fun initRecyclerView() {
        binding.rvPlanner.adapter = plannerAdapter
        viewModel.loadAllPlanner()
    }

    private fun onClickPlanner(planner: BasePlanner) {
        when (val activity = requireActivity()) {
            is HomeActivity -> {
                activity.initPlannerData(planner)
            }
        }
    }

    private fun onClickAdd() {

    }

}