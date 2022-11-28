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
import android.widget.RadioGroup
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
        binding.activity = this
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
                    when(it.reason) {
                        "existAccount" -> {
                            binding.tvError.text = "이미 존재하는 계정입니다."

                        }
                        else -> {
                            binding.tvError.text = viewModel.registerErrorCheck()
                        }
                    }
                }
            }
        }
        viewModel.loginState.observe(this) {
            when(it) {
                is LoginState.Loading -> {
                    loginLoadingView()
                }
                is LoginState.Success -> {
                    viewModel.updateUserToken()
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                is LoginState.Error -> {
                    loginErrorView()
                }
            }
        }
        viewModel.isCheckNickname.observe(this) { duplicated ->
            when(duplicated) {
                true -> {
                    checkSuccessView()
                }
                false -> {
                    checkFailView()
                }
            }
        }
    }

    private fun checkSuccessView() {
        binding.etNickname.visibility = View.GONE
        binding.btnCheck.visibility = View.GONE
        binding.tvNicknameFix.visibility = View.VISIBLE
        binding.tvNicknameFix.text = viewModel.nickname.value.toString()
        binding.registerActCheckBtnSuccess.visibility = View.VISIBLE
    }

    private fun checkFailView() {
        binding.etNickname.visibility = View.VISIBLE
        binding.btnCheck.visibility = View.VISIBLE
        binding.tvNicknameFix.visibility = View.GONE
        binding.registerActCheckBtnSuccess.visibility = View.GONE
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

    fun onSplitTypeChanged(radioGroup: RadioGroup, id: Int) {
        when (id) {
            binding.rbtnMan.id -> {
                binding.rbtnMan.run {
                    setBackgroundResource(R.drawable.shape_btn_round_recommend)
                    setTextColor(Color.parseColor("#1a83e6"))
                }
                binding.rbtnWoman.run {
                    setBackgroundResource(R.drawable.shape_register)
                    setTextColor(Color.parseColor("#616161"))
                }
                viewModel.gender.value = 0
            }
            binding.rbtnWoman.id -> {
                binding.rbtnMan.run {
                    setBackgroundResource(R.drawable.shape_register)
                    setTextColor(Color.parseColor("#616161"))
                }
                binding.rbtnWoman.run {
                    setBackgroundResource(R.drawable.shape_btn_round_recommend)
                    setTextColor(Color.parseColor("#1a83e6"))
                }
                viewModel.gender.value = 1
            }
        }
    }

}