package com.paran.presentation.views.fragment

import android.os.Bundle
import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.databinding.FragmentPlannerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlannerFragment : BaseETFragment<FragmentPlannerBinding>(R.layout.fragment_planner) {
    companion object {
        fun newInstance(): PlannerFragment = PlannerFragment().apply {
            arguments = Bundle().apply {

            }
        }

        private const val TAG = "PlannerFrag"
    }

    override fun init() {
//        TODO("Not yet implemented")
    }

}