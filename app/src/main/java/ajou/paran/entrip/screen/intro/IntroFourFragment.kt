package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentIntroFourBinding
import ajou.paran.entrip.screen.home.HomeActivity
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
        private const val TAG = "[IntroFourFragment]"
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
        tokenCheck()
        binding.btnSocialLogin.setOnClickListener {
            socialLogin()
        }
        subscribeObservers()
    }

    private fun tokenCheck() {
        when (viewModel.isTokenNull()) {
            true -> {
                AlertDialog.Builder(activity!!)
                    .setMessage("앱을 재시작 해주세요.")
                    .setPositiveButton("확인") { dialog, which ->
                        activity!!.finishAffinity()
                        System.runFinalization()
                        System.exit(0)
                    }.show()
            }
            false -> { }
        }
    }

    private fun subscribeObservers() {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoginState.Loading -> {
                    loadingView()
                }
                is LoginState.Success -> {
                    Log.d(TAG, "Login Success")
                    viewingView()
                    binding.tvError.visibility = View.GONE
                    startActivity(Intent(context, HomeActivity::class.java))
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
        Log.d(TAG, "Case: Click Login")
        getResult.launch(googleSignInClient.signInIntent)
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