package ajou.paran.entrip.screen.intro.register

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityRegisterBinding
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity
: BaseActivity<ActivityRegisterBinding>(R.layout.activity_register), View.OnClickListener{
    companion object{
        const val TAG = "[RegisterActivity]"
    }

    private val viewModel: RegisterActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        binding.registerActNickname.setText(viewModel.getUserId())
    }

    override fun onClick(view: View?) {
        view?.let {
            when(it.id){
                binding.registerActCheckBtn.id -> {

                }
                binding.registerActEndBtn.id -> {

                }
            }
        }
    }
}