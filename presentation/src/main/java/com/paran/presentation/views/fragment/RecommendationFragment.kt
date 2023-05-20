package com.paran.presentation.views.fragment

import android.os.Bundle
import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.databinding.FragmentPlannerBinding
import com.paran.presentation.databinding.FragmentRecommendationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendationFragment : BaseETFragment<FragmentRecommendationBinding>(R.layout.fragment_recommendation) {
    companion object {
        fun newInstance(): RecommendationFragment = RecommendationFragment().apply {
            arguments = Bundle().apply {

            }
        }

        private const val TAG = "RecommFrag"
    }

    override fun init() {
//        TODO("Not yet implemented")
    }

}