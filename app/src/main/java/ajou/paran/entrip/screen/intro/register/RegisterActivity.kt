package ajou.paran.entrip.screen.intro.register

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityRegisterBinding
import ajou.paran.entrip.screen.intro.IntroFourFragment
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import ajou.paran.entrip.util.network.BaseResult
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity
: BaseActivity<ActivityRegisterBinding>(R.layout.activity_register), View.OnClickListener{
    companion object{
        const val TAG = "[RegisterActivity]"
    }

    private val viewModel: RegisterActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        binding.registerActUserId.text = viewModel.getUserId()
        subscribeObservers()
    }

    override fun onClick(view: View?) {
        view?.let {
            when(it.id){
                binding.registerActCheckBtn.id -> {
                    viewModel.setIsExistNickname(binding.registerActNickname.text.toString())
                }
                binding.registerActEndBtn.id -> {

                }
                else -> {
                    return
                }
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.getIsExistNickname().observe(this, Observer {
            if (it is BaseResult.Success){
                if (it.data){
                    // 이미 존재하는 닉네임
                    Log.d(TAG, "이미 존재하는 닉네임")
                } else {
                    // 존재하지 않는 닉네임
                    Log.d(TAG, "존재하지 않는 닉네임")
                }
            } else {
                Log.e(IntroFourFragment.TAG, "code: ${(it as BaseResult.Error).err.code}, message: ${it.err.message}")
            }
        })
    }
}