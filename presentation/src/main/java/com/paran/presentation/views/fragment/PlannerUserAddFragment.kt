package com.paran.presentation.views.fragment

import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.databinding.FragmentPlannerUserAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlannerUserAddFragment : BaseETFragment<FragmentPlannerUserAddBinding>(R.layout.fragment_planner_user_add) {
    companion object {
        fun newInstance(): PlannerUserAddFragment = PlannerUserAddFragment().apply {

        }

        private const val TAG = "UserAddFrag"
    }

    override fun init() {
//        TODO("Not yet implemented")
    }

}