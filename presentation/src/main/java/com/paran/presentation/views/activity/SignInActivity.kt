package com.paran.presentation.views.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETActivity
import com.paran.presentation.databinding.ActivitySignInBinding
import com.paran.presentation.views.viewmodel.SignInActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BaseETActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {
    companion object {
        private const val TAG = "SignInAct"
    }

    private val signInViewModel: SignInActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        binding.activity = this
        binding.viewModel = signInViewModel
    }

    fun onClickSignUp(view: View) {
        startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        finish()
    }

    fun onClickHide(view: View) = binding.run {
        signInIvShow.visibility = View.VISIBLE
        signInIvHide.visibility = View.GONE
        signInEtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        signInEtPassword.setSelection(signInEtPassword.length());
    }

    fun onClickShow(view: View) = binding.run {
        signInIvShow.visibility = View.GONE
        signInIvHide.visibility = View.VISIBLE
        signInEtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        signInEtPassword.setSelection(signInEtPassword.length());
    }

    fun hideKeyboard(view: View, event: MotionEvent): Boolean {
        val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            inputManager.hideSoftInputFromWindow(
                it.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
        return false
    }
}