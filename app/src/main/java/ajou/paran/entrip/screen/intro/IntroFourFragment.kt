package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentIntroFourBinding
import ajou.paran.entrip.repository.network.dto.PlannerResponse
import ajou.paran.entrip.repository.network.dto.UserResponse
import ajou.paran.entrip.screen.home.HomeActivity
import ajou.paran.entrip.screen.intro.register.RegisterActivity
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
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

    private val viewModel: IntroFragmentViewModel by viewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    lateinit var getResult: ActivityResultLauncher<Intent>
    private lateinit var user_id: String

    override fun init() {

        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                handleSignInResult(task)
            }else{
                val builder = AlertDialog.Builder(activity!!)
                builder.setMessage("??????????????? ??????????????????")
                    .setPositiveButton("??????",
                        DialogInterface.OnClickListener{ dialog, which -> })
                builder.show()
            }
        }

        animating()

        binding.introFourActBtnLogin.setOnClickListener{
            Log.d(IntroThreeFragment.TAG, "Case: Click Login")
            getResult.launch(googleSignInClient.signInIntent)
        }
        binding.introFourActBtnNext.setOnClickListener{
            Log.d(IntroThreeFragment.TAG, "Case: Click Next")
            startActivity(Intent(activity, HomeActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        animating()
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

    private fun subscribeObservers() {
        viewModel.result(user_id)
        lifecycleScope.launchWhenStarted {
            viewModel.isExistUserResult.collect {
                if (it is ApiState.Success){
                    // ???????????? ?????? ?????????
                    val intent = Intent(context, RegisterActivity::class.java)
                    intent.putExtra("user_id", user_id)
                    startActivity(intent)
                } else if (it is ApiState.Failure) {
                    if (it.code == 999){
                        // ?????? ???????????? ?????????
                        viewModel.findById(user_id)
                        viewModel.getUserPlanners(user_id)
                    } else {
                        Log.e(
                            TAG,
                            "code: ${it.code}"
                        )
                    }
                } else if (it is ApiState.Init) {
                    Log.d(TAG, "observe ??????")
                }
                else {
                    Log.e(
                        TAG,
                        "?????? ?????? ??????"
                    )
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.getUserPlannersResult.collect { res ->
                when (res) {
                    is ApiState.Success -> {
                        Log.d(TAG, "??????")
                        startActivity(Intent(context, HomeActivity::class.java))
                    }
                    is ApiState.Init -> {
                        Log.d(TAG, "planners observe ??????")
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
                            "?????? ?????? ??????"
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
                                Log.d(TAG, "?????? ?????? sharedPreference ?????? ??????")
                            }
                            is BaseResult.Error<*> -> {
                                Log.e(TAG, res.data.err.toString())
                            }
                            else -> {}
                        }



                    }
                    is ApiState.Failure -> {
                        Log.e(
                            TAG,
                            "code: ${res.code}"
                        )
                    }
                    else ->{ }
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
            googleSignInClient.signOut()
        } catch (e: ApiException){
            Log.e(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

}