package com.paran.presentation.views.fragment

import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.databinding.FragmentVoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VoteFragment : BaseETFragment<FragmentVoteBinding>(R.layout.fragment_vote) {
    companion object {
        fun newInstance(): VoteFragment = VoteFragment().apply {

        }

        private const val TAG = "VoteFrag"
    }

    override fun init() {
//        TODO("Not yet implemented")
    }

}