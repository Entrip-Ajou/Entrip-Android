package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentIntroFourBinding
import ajou.paran.entrip.screen.intro.register.RegisterActivity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroFourFragment: BaseFragment<FragmentIntroFourBinding>(R.layout.fragment_intro_four) {
    companion object{
        const val TAG = "[IntroFourFragment]"
    }
    private val viewModel: IntroFragmentViewModel by viewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    lateinit var getResult: ActivityResultLauncher<Intent>

    override fun init() {
        binding.fragment = this
        binding.viewModel = viewModel
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when(it.resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    handleSignInResult(task)
                }
                else -> {
                    val builder = AlertDialog.Builder(activity!!)
                    builder.setMessage("네트워크를 확인해주세요")
                        .setPositiveButton("확인") { dialog, which -> dialog.dismiss() }
                    builder.show()
                }
            }
        }
        binding.btnSocialLogin.setOnClickListener {
            socialLogin()
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        /*
        viewModel.result(user_id)
        lifecycleScope.launchWhenStarted {
            viewModel.isExistUserResult.collect {
                when(it) {
                    is ApiState.Success -> {
                        // 존재하지 않는 아이디
                        val intent = Intent(context, RegisterActivity::class.java)
                        intent.putExtra("user_id", user_id)
                        startActivity(intent)
                    }
                    is ApiState.Failure -> {
                        when(it.code) {
                            999 -> {
                                // 이미 존재하는 아이디
                                viewModel.findById(user_id)
                                viewModel.getUserPlanners(user_id)
                            }
                            else -> { Log.e(TAG, "code: ${it.code}") }
                        }
                    }
                    is ApiState.Init -> { Log.d(TAG, "observe 시작") }
                    else -> { Log.e(TAG, "예상 못한 에러") }
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.getUserPlannersResult.collect { res ->
                when (res) {
                    is ApiState.Success -> {
                        Log.d(TAG, "성공")
                        startActivity(Intent(context, HomeActivity::class.java))
                    }
                    is ApiState.Init -> {
                        Log.d(TAG, "planners observe 시작")
                    }
                    is ApiState.Failure -> {
                        Log.e(
                            TAG,
                            "code: ${res.code}"
                        )
                        startActivity(Intent(context, HomeActivity::class.java))
                    }
                    else -> {
                        Log.e(
                            TAG,
                            "예상 못한 에러"
                        )
                    }
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.findByIdResult.collect{ res ->
                when(res){
                    is ApiState.Success ->{
                        when(res.data){
                            is BaseResult.Success<*> ->{
                                val user_id = (res.data.data as UserResponse).userId
                                val photo_url = res.data.data.photoUrl
                                val nickname = res.data.data.nickname
                                viewModel.commitShared(
                                    user_id = user_id,
                                    photo_url = photo_url,
                                    nickname = nickname
                                )
                                Log.d(TAG, "기존 회원 sharedPreference 복구 완료")
                            }
                            is BaseResult.Error<*> -> { Log.e(TAG, res.data.err.toString()) }
                            else -> {  }
                        }
                    }
                    is ApiState.Failure -> { Log.e(TAG, "code: ${res.code}") }
                    else -> { }
                }
            }
        }
        */
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoginState.Loading -> {
                    loadingView()
                }
                is LoginState.Success -> {
                    Log.d(TAG, "Login Success")
                    viewingView()
                    binding.tvError.visibility = View.GONE
                }
                is LoginState.Error -> {
                    viewingView()
                    binding.tvError.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun loadingView() {
        binding.layoutProgress.visibility = View.VISIBLE
        binding.layoutContent.visibility = View.GONE
    }

    private fun viewingView() {
        binding.layoutProgress.visibility = View.GONE
        binding.layoutContent.visibility = View.VISIBLE
    }

    fun moveRegister() {
        val intent = Intent(context, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun socialLogin() {
        Log.d(IntroThreeFragment.TAG, "Case: Click Login")
        if(!viewModel.isTokenNull()) {
            getResult.launch(googleSignInClient.signInIntent)
        } else {
            val builder = AlertDialog.Builder(activity!!)
                .setMessage("앱을 재시작 해주세요.")
                .setPositiveButton("확인") { dialog, which ->
                    activity!!.finishAffinity()
                    System.runFinalization()
                    System.exit(0)
                }
            builder.show()
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account  = completedTask.getResult(ApiException::class.java)
            Log.d(TAG, account.email.toString())
            viewModel.userId.value = account.email.toString()
            viewModel.userPassword.value = "${account.email.toString()}_2ntrip.com"
            viewModel.loginUserAccount()
            googleSignInClient.signOut()
        } catch (e: ApiException){
            Log.e(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

}