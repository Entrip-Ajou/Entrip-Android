package ajou.paran.entrip.screen.intro.login

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityLoginBinding
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


@AndroidEntryPoint
class LoginActivity
    : BaseActivity<ActivityLoginBinding>(R.layout.activity_login),
        View.OnClickListener {
    companion object{
        const val TAG = "[LoginActivity]"
    }

    override fun init(savedInstanceState: Bundle?) {

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
                binding.loginActButtonRegister.id -> {
                    Log.d(TAG, "Case: Click Register")
                }
                else -> {

                }
            }
        }
    }

}