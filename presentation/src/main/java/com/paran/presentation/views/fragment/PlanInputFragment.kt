package com.paran.presentation.views.fragment

import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.databinding.FragmentPlanInputBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlanInputFragment : BaseETFragment<FragmentPlanInputBinding>(R.layout.fragment_plan_input) {
    companion object {
        fun newInstance(): PlanInputFragment = PlanInputFragment().apply {

        }

        private const val TAG = "PlanInputFrag"
    }

    override fun init() {
//        TODO("Not yet implemented")
    }

}