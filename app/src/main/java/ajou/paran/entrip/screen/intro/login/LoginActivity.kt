package ajou.paran.entrip.screen.intro.login

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityLoginBinding
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope

import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity
    : BaseActivity<ActivityLoginBinding>(R.layout.activity_login),
        View.OnClickListener {
    companion object{
        const val TAG = "[LoginActivity]"
    }

    @Inject lateinit var googleSignInClient: GoogleSignInClient

    private val viewModel: LoginActivityViewModel by viewModels()
    private lateinit var getResult: ActivityResultLauncher<Intent>

    override fun init(savedInstanceState: Bundle?) {
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                handleSignInResult(task)
            }
        }
    }

    override fun onClick(view: View?) {
        view?.let {
            when(it.id){
                binding.loginActButtonLogin.id -> {
                    startActivity(Intent(this, PlannerActivity::class.java))
                    val userInsertId = binding.loginActEditTextId.text.toString()
                    val userInsertPass = binding.loginActEditTextPassword.text.toString()
                    val data = JSONObject()
                    data.put("userId", userInsertId)
                    data.put("userPassword", userInsertPass)
                }
                binding.loginActButtonGoogle.id -> {
                    Log.d(TAG, "Case: Click google Login")
                    getResult.launch(googleSignInClient.signInIntent)
                }
                else -> {

                }
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account  = completedTask.getResult(ApiException::class.java)
            Log.d(TAG, account.email.toString())

            lifecycleScope.launch {
                viewModel.isExistUser(user_id = account.email.toString()).collect {
                    if (it == 0){
                        viewModel.insertUserId(account.email.toString())
                        startActivity(Intent(this@LoginActivity, PlannerActivity::class.java))
                    }
                }
            }
        } catch (e: ApiException){
            Log.e(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }
}