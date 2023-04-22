package com.paran.presentation.views.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETActivity
import com.paran.presentation.databinding.ActivitySignUpBinding
import com.paran.presentation.views.viewmodel.SignUpActivityViewModel

class SignUpActivity : BaseETActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    companion object {
        private const val TAG = "SignUpAct"
    }

    private val signUpActivityViewModel: SignUpActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        binding.activity = this
        binding.viewModel = signUpActivityViewModel
    }

}