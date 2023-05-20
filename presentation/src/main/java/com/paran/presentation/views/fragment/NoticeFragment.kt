package com.paran.presentation.views.fragment

import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.databinding.FragmentNoticeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeFragment : BaseETFragment<FragmentNoticeBinding>(R.layout.fragment_notice) {
    companion object {
        fun newInstance(): NoticeFragment = NoticeFragment().apply {

        }

        private const val TAG = "NoticeFrag"
    }

    override fun init() {
//        TODO("Not yet implemented")
    }

}