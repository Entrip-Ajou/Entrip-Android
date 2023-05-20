package com.paran.presentation.views.fragment

import android.os.Bundle
import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETFragment
import com.paran.presentation.databinding.FragmentMyPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseETFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    companion object {
        fun newInstance(): MyPageFragment = MyPageFragment().apply {
            arguments = Bundle().apply {

            }
        }

        private const val TAG = "MyPageFrag"
    }

    override fun init() {
//        TODO("Not yet implemented")
    }

}