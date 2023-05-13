package com.paran.presentation.views.fragment

import ajou.paran.domain.model.BasePlanner
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.paran.presentation.R
import com.paran.presentation.common.adapter.HomePlannerAdapter
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.databinding.FragmentHomeBinding
import com.paran.presentation.views.activity.HomeActivity
import com.paran.presentation.views.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseETFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    companion object {
        fun newInstance(): HomeFragment = HomeFragment().apply {
            arguments = Bundle().apply {

            }
        }

        private const val TAG = "HomeFrag"
    }

    private val viewModel: HomeFragmentViewModel by viewModels()

    private val plannerAdapter: HomePlannerAdapter = HomePlannerAdapter(this::onPlannerClickListener)

    override fun init() {
        setUpRecyclerView()
        subObserver()
        viewModel.loadAllPlanner()
    }

    private fun setUpRecyclerView() {
        binding.homeRvPlanner.run {
            adapter = plannerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun subObserver() {
        viewModel.plannerList.observe(this) {
            when (it.isEmpty()) {
                true -> {
                    binding.homeClPlanner.visibility = View.VISIBLE
                    binding.homeRvPlanner.visibility = View.GONE
                }
                false -> {
                    binding.homeClPlanner.visibility = View.GONE
                    binding.homeRvPlanner.visibility = View.VISIBLE
                    plannerAdapter.submitList(it)
                }
            }
        }
    }

    private fun onPlannerClickListener(planner: BasePlanner) {
        when (val activity = requireActivity()) {
            is HomeActivity -> {
                activity.initPlannerData(planner)
            }
        }
    }

}