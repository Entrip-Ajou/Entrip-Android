package com.paran.presentation.views.fragment

import android.os.Bundle
import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.databinding.FragmentCommunityBinding
import com.paran.presentation.databinding.FragmentPlannerBinding
import com.paran.presentation.databinding.FragmentRecommendationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment : BaseETFragment<FragmentCommunityBinding>(R.layout.fragment_community) {
    companion object {
        fun newInstance(): CommunityFragment = CommunityFragment().apply {
            arguments = Bundle().apply {

            }
        }
        private const val TAG = "CommunityFrag"
    }

    override fun init() {
//        TODO("Not yet implemented")
    }

}