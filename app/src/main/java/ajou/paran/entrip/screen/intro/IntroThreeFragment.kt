package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentIntroThreeBinding
import ajou.paran.entrip.screen.intro.login.LoginActivity
import ajou.paran.entrip.screen.intro.register.RegisterActivity
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import ajou.paran.entrip.util.network.BaseResult
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IntroThreeFragment: BaseFragment<FragmentIntroThreeBinding>(R.layout.fragment_intro_three) {
    companion object{
        const val TAG = "[IntroThreeFragment]"
    }

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    private val viewModel: IntroFragmentViewModel by viewModels()
    private val getResult: ActivityResultLauncher<Intent>
        = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == AppCompatActivity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handleSignInResult(task)
        } else {
            /*테스트용*/
            viewModel.insertUserId("heekyne66@gmail.com")
            startActivity(Intent(context, RegisterActivity::class.java))
        }
    }

    override fun init() {
        binding.introThreeActBtnLogin.setOnClickListener{
            Log.d(TAG, "Case: Click Login")
            getResult.launch(googleSignInClient.signInIntent)
        }
        binding.introThreeActBtnNext.setOnClickListener{
            Log.d(TAG, "Case: Click Next")
            startActivity(Intent(activity, PlannerActivity::class.java))
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.existUserResult.observe(viewLifecycleOwner){
            if (it is BaseResult.Success){
                if (it.data){
                    // 이미 존재하는 아이디
                    startActivity(Intent(context, PlannerActivity::class.java))
                } else {
                    // 존재하지 않는 아이디
                    startActivity(Intent(context, RegisterActivity::class.java))
                }
            } else {
                TODO("네트워크 오류 발생")
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account  = completedTask.getResult(ApiException::class.java)
            Log.d(LoginActivity.TAG, account.email.toString())
            viewModel.insertUserId(account.email.toString())
        } catch (e: ApiException){
            Log.e(LoginActivity.TAG, "signInResult:failed code=" + e.statusCode)
        }
    }
}