package com.paran.presentation.views.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioGroup
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETActivity
import com.paran.presentation.databinding.ActivitySignUpBinding
import com.paran.presentation.utils.state.SignUpState
import com.paran.presentation.views.viewmodel.SignUpActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseETActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    companion object {
        private const val TAG = "SignUpAct"
    }

    private val signUpActivityViewModel: SignUpActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        binding.activity = this
        binding.viewModel = signUpActivityViewModel
        initSubObserver()
    }

    private fun initSubObserver() {
        signUpActivityViewModel.isNotExistUser.observe(this) { result ->
            if (result) {
                binding.signUpTvEmail.visibility = View.VISIBLE
                binding.signUpEtEmail.visibility = View.GONE
            }
        }
        signUpActivityViewModel.isNotExistNickname.observe(this) { result ->
            if (result) {
                binding.signUpTvNickname.visibility = View.VISIBLE
                binding.signUpEtNickname.visibility = View.GONE
            }
        }
        signUpActivityViewModel.errorMessage.observe(this) {
            when (it.isEmpty()) {
                true -> hideKeyboard()
                false -> {
                    snackBar(it)
                    hideKeyboard()
                }
            }
        }
        signUpActivityViewModel.routeState.observe(this) { state ->
            when (state) {
                is SignUpState.Loading -> {
                    binding.signUpLoading.visibility = View.VISIBLE
                    binding.signUpContent.visibility = View.GONE
                }
                is SignUpState.Success -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                else -> {
                    binding.signUpLoading.visibility = View.GONE
                    binding.signUpContent.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun snackBar(msg: String) = Snackbar
        .make(binding.root, msg, Snackbar.LENGTH_SHORT)
        .setAction("확인") {
            signUpActivityViewModel.clearErrorMessage()
        }.show()

    //region onClick
    fun onClickPasswordHide(view: View) = binding.run {
        signUpPasswordIvShow.visibility = View.VISIBLE
        signUpPasswordIvHide.visibility = View.GONE
        signUpEtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        signUpEtPassword.setSelection(signUpEtPassword.length());
    }

    fun onClickPasswordShow(view: View) = binding.run {
        signUpPasswordIvShow.visibility = View.GONE
        signUpPasswordIvHide.visibility = View.VISIBLE
        signUpEtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        signUpEtPassword.setSelection(signUpEtPassword.length());
    }

    fun onClickPasswordCheckHide(view: View) = binding.run {
        signUpPasswordCheckIvShow.visibility = View.VISIBLE
        signUpPasswordCheckIvHide.visibility = View.GONE
        signUpEtPasswordCheck.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        signUpEtPasswordCheck.setSelection(signUpEtPasswordCheck.length());
    }

    fun onClickPasswordCheckShow(view: View) = binding.run {
        signUpPasswordCheckIvShow.visibility = View.GONE
        signUpPasswordCheckIvHide.visibility = View.VISIBLE
        signUpEtPasswordCheck.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        signUpEtPasswordCheck.setSelection(signUpEtPasswordCheck.length());
    }

    fun onSplitTypeChanged(radioGroup: RadioGroup, id: Int) {
        when (id) {
            binding.signUpBtnMan.id -> {
                binding.signUpBtnMan.run {
                    setBackgroundResource(R.drawable.shape_btn_round_recommend)
                    setTextColor(Color.parseColor("#1a83e6"))
                }
                binding.signUpBtnWoman.run {
                    setBackgroundResource(R.drawable.shape_register)
                    setTextColor(Color.parseColor("#616161"))
                }
                signUpActivityViewModel.onClickGender(0)
//                viewModel.gender.value = 0
            }
            binding.signUpBtnWoman.id -> {
                binding.signUpBtnMan.run {
                    setBackgroundResource(R.drawable.shape_register)
                    setTextColor(Color.parseColor("#616161"))
                }
                binding.signUpBtnWoman.run {
                    setBackgroundResource(R.drawable.shape_btn_round_recommend)
                    setTextColor(Color.parseColor("#1a83e6"))
                }
                signUpActivityViewModel.onClickGender(1)
//                viewModel.gender.value = 1
            }
        }
    }

    fun hideKeyboard(
        view: View? = null,
        event: MotionEvent? = null,
    ): Boolean {
        val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            inputManager.hideSoftInputFromWindow(
                it.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
        return false
    }

    //endregion
}