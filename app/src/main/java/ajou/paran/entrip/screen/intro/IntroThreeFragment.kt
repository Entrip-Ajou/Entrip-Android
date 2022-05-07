package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentIntroThreeBinding
import ajou.paran.entrip.screen.intro.login.LoginActivity
import ajou.paran.entrip.screen.intro.register.RegisterActivity
import ajou.paran.entrip.screen.planner.top.PlannerActivity
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
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account  = completedTask.getResult(ApiException::class.java)
            Log.d(LoginActivity.TAG, account.email.toString())

            lifecycleScope.launch {
                viewModel.isExistUser(user_id = account.email.toString()).collect {
                    if (it == 0){
                        viewModel.insertUserId(account.email.toString())
                        startActivity(Intent(context, RegisterActivity::class.java))
                    }
                }
            }
        } catch (e: ApiException){
            Log.e(LoginActivity.TAG, "signInResult:failed code=" + e.statusCode)
        }
    }
}