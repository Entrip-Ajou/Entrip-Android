package com.paran.presentation.views.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.paran.presentation.R
import com.paran.presentation.common.adapter.IntroFragmentAdapter
import com.paran.presentation.common.base.BaseETActivity
import com.paran.presentation.databinding.ActivityIntroBinding
import com.paran.presentation.utils.state.IntroState
import com.paran.presentation.views.fragment.IntroFirstFragment
import com.paran.presentation.views.fragment.IntroSecondFragment
import com.paran.presentation.views.fragment.IntroThirdFragment
import com.paran.presentation.views.viewmodel.IntroActivityViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IntroActivity: BaseETActivity<ActivityIntroBinding>(R.layout.activity_intro) {
    companion object {
        private const val TAG = "IntroAct"
    }

    private val viewModel: IntroActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        binding.activity = this
        subObserver()
        initViewPager()
    }

    private fun subObserver() {
        viewModel.routeState.observe(this) { state ->
            when (state) {
                is IntroState.SignIn -> {
                    startActivity(Intent(this@IntroActivity, SignInActivity::class.java))
                    finish()
                }
                else -> {}
            }
        }
    }

    private fun initViewPager() = binding.run {
        binding.introVp.adapter = IntroFragmentAdapter(this@IntroActivity).apply {
            addFragmentList(
                listOf(
                    IntroFirstFragment(),
                    IntroSecondFragment(),
                    IntroThirdFragment(),
                )
            )
        }
        dotsIndicator.setViewPager2(introVp)
    }

    fun onClickExit(view: View) {
        viewModel.saveIsEntry()
    }
}