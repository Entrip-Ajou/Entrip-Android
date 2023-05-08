package com.paran.presentation.views.fragment

import ajou.paran.domain.model.BasePlanner
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.paran.presentation.R
import com.paran.presentation.common.adapter.HomePlannerAdapter
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.databinding.FragmentHomeBinding
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

    override fun init() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val plannerAdapter = HomePlannerAdapter(this::onPlannerClickListener)
        binding.homeRvPlanner.run {
            adapter = plannerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun onPlannerClickListener(planner: BasePlanner): Unit = startActivity(
        TODO("onPlannerClickListener")
//        Intent().apply {
//
//        }
    )

}