package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentIntroFourBinding
import ajou.paran.entrip.screen.intro.register.RegisterActivity
import ajou.paran.entrip.screen.planner.main.MainActivity
import ajou.paran.entrip.util.ApiState
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IntroFourFragment: BaseFragment<FragmentIntroFourBinding>(R.layout.fragment_intro_four) {
    companion object{
        const val TAG = "[IntroFourFragment]"
    }

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    private val viewModel: IntroFragmentViewModel by viewModels()
    private lateinit var getResult: ActivityResultLauncher<Intent>

    private lateinit var user_id: String

    override fun init() {
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            Log.d(TAG, GoogleSignIn.getSignedInAccountFromIntent(it.data).getResult(ApiException::class.java).email.toString())
            if (it.resultCode == AppCompatActivity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                handleSignInResult(task)
            } else {
                /*테스트용*/
                user_id = "test"
                subscribeObservers()
            }
        }

        animating()

        binding.introFourActBtnLogin.setOnClickListener{
            Log.d(IntroThreeFragment.TAG, "Case: Click Login")
            getResult.launch(googleSignInClient.signInIntent)
        }
        binding.introFourActBtnNext.setOnClickListener{
            Log.d(IntroThreeFragment.TAG, "Case: Click Next")
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        animating()
    }

    private fun subscribeObservers() {
        viewModel.result(user_id)
        lifecycleScope.launchWhenStarted {
            viewModel.isExistUserResult.collect {
                if (it is ApiState.Success){
                    // 존재하지 않는 아이디
                    val intent = Intent(context, RegisterActivity::class.java)
                    intent.putExtra("user_id", user_id)
                    startActivity(intent)
                } else if (it is ApiState.Failure) {
                    if (it.code == 999){
                        // 이미 존재하는 아이디
                        viewModel.userIdShared(user_id)
                        startActivity(Intent(context, MainActivity::class.java))
                    } else {
                        Log.e(
                            TAG,
                            "code: ${it.code}"
                        )
                    }
                } else if (it is ApiState.Init) {
                    Log.d(TAG, "observe 시작")
                }
                else {
                    Log.e(
                        TAG,
                        "예상 못한 에러"
                    )
                }
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account  = completedTask.getResult(ApiException::class.java)
            Log.d(TAG, account.email.toString())
            user_id = account.email.toString()
            subscribeObservers()
        } catch (e: ApiException){
            Log.e(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun animating() = CoroutineScope(Dispatchers.Main).launch {
        binding.introFourActBtnLogin.visibility = View.INVISIBLE
        binding.introFourActBtnNext.visibility = View.INVISIBLE
        val animation = TranslateAnimation(0f, 0f, 200f, 0f)
        animation.duration = 1000
        animation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
            }
            override fun onAnimationEnd(p0: Animation?) {
                binding.introFourActBtnLogin.visibility = View.VISIBLE
                binding.introFourActBtnNext.visibility = View.VISIBLE
            }
            override fun onAnimationRepeat(p0: Animation?) {}
        })
        binding.introFourActText.startAnimation(animation)
    }
}