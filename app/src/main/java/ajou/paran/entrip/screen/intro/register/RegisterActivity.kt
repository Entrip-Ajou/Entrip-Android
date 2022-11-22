package ajou.paran.entrip.screen.intro.register

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityRegisterBinding
import ajou.paran.entrip.screen.home.HomeActivity
import ajou.paran.entrip.screen.intro.LoginState
import ajou.paran.entrip.screen.intro.RegisterState
import ajou.paran.entrip.util.ApiState
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding>(R.layout.activity_register) {
    companion object{
        const val TAG = "[RegisterActivity]"
    }

    private val viewModel: RegisterActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.registerState.observe(this) {
            when(it) {
                is RegisterState.Loading -> {
                    registerLoadingView()
                }
                is RegisterState.Success -> {
                    registerViewingView()
                    viewModel.loginUserAccount(userId = it.userId, password = it.userPassword)
                }
                is RegisterState.Error -> {
                    registerViewingView()
                    binding.tvError.text = viewModel.registerErrorCheck()
                }
            }
        }
        viewModel.loginState.observe(this) {
            when(it) {
                is LoginState.Loading -> {
                    loginLoadingView()
                }
                is LoginState.Success -> {

                }
                is LoginState.Error -> {
                    loginErrorView()
                }
            }
        }
    }

    private fun registerLoadingView() {
        binding.layoutContent.visibility = View.GONE
        binding.layoutProgress.visibility = View.VISIBLE
        binding.layoutLogin.visibility = View.GONE
    }

    private fun registerViewingView() {
        binding.layoutContent.visibility = View.VISIBLE
        binding.layoutProgress.visibility = View.GONE
        binding.layoutLogin.visibility = View.GONE
    }

    private fun loginLoadingView() {
        binding.layoutContent.visibility = View.GONE
        binding.layoutProgress.visibility = View.GONE
        binding.layoutLogin.visibility = View.VISIBLE
        binding.btnLoginRefresh.visibility = View.GONE
    }

    private fun loginErrorView() {
        binding.layoutContent.visibility = View.GONE
        binding.layoutProgress.visibility = View.GONE
        binding.layoutLogin.visibility = View.VISIBLE
        binding.btnLoginRefresh.visibility = View.VISIBLE
    }

    fun repeatedAttempt() {

    }

}