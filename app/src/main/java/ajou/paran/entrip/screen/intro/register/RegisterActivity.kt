package ajou.paran.entrip.screen.intro.register

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityRegisterBinding
import ajou.paran.entrip.screen.home.HomeActivity
import ajou.paran.entrip.util.ApiState
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity
    : BaseActivity<ActivityRegisterBinding>(R.layout.activity_register), View.OnClickListener{
    companion object{
        const val TAG = "[RegisterActivity]"
    }

    private val viewModel: RegisterActivityViewModel by viewModels()

    private var endCondition = false

    override fun init(savedInstanceState: Bundle?) {
        viewModel.user_id = intent.getStringExtra("user_id")!!
        binding.registerActUserId.text = viewModel.user_id
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when(checkedId){
                binding.registerActRadioMan.id -> {
                    binding.registerActRadioMan.setBackgroundResource(R.drawable.shape_btn_round_recommend)
                    binding.registerActRadioMan.setTextColor(Color.parseColor("#1a83e6"))
                    binding.registerActRadioWoman.setBackgroundResource(R.drawable.shape_register)
                    binding.registerActRadioWoman.setTextColor(Color.parseColor("#616161"))
                }
                binding.registerActRadioWoman.id -> {
                    binding.registerActRadioMan.setBackgroundResource(R.drawable.shape_register)
                    binding.registerActRadioMan.setTextColor(Color.parseColor("#616161"))
                    binding.registerActRadioWoman.setBackgroundResource(R.drawable.shape_btn_round_recommend)
                    binding.registerActRadioWoman.setTextColor(Color.parseColor("#1a83e6"))
                }
                else -> {
                    binding.registerActRadioMan.setBackgroundResource(R.drawable.shape_register)
                    binding.registerActRadioMan.setTextColor(Color.parseColor("#616161"))
                    binding.registerActRadioWoman.setBackgroundResource(R.drawable.shape_register)
                    binding.registerActRadioWoman.setTextColor(Color.parseColor("#616161"))
                }
            }
        }
    }

    override fun onClick(view: View?) {
        view?.let { view ->
            when(view.id){
                binding.registerActCheckBtn.id -> {
                    viewModel.nickNameResult(binding.registerActEtNickname.text.toString())
                    observeNickname()
                }
                binding.registerActEndBtn.id -> {
                    if (endCondition){
                        if(binding.radioGroup.checkedRadioButtonId == binding.registerActRadioMan.id) {
                            viewModel.saveUserResult(0, binding.registerActTvNickname.text.toString())
                            observeSave()
                        }
                        else if (binding.radioGroup.checkedRadioButtonId == binding.registerActRadioWoman.id){
                            viewModel.saveUserResult(1, binding.registerActTvNickname.text.toString())
                            observeSave()
                        }
                        else if (binding.radioGroup.checkedRadioButtonId == -1){
                            binding.registerActRadioMan.setBackgroundResource(R.drawable.shape_register_error)
                            binding.registerActRadioWoman.setBackgroundResource(R.drawable.shape_register_error)
                        }
                        else {
                            Log.d(TAG, "????????? ??? ????????? ?????? ?????????")
                        }
                    } else {
                        Log.d(TAG, "???????????? ??????")
                        binding.registerActEtNickname.setBackgroundResource(R.drawable.shape_register_error)
                    }
                }
                else -> {
                    return
                }
            }
        }
    }

    private fun observeNickname() = lifecycleScope.launchWhenStarted {
        viewModel.isExistNicknameResult.collect {
            if (it is ApiState.Success){
                // ???????????? ?????? ?????????
                Log.d(TAG, "???????????? ?????? ?????????")
                endCondition = true
                val builder = AlertDialog.Builder(this@RegisterActivity)
                builder.setMessage("???????????? ?????? ??????????????????")
                    .setPositiveButton("??????",
                        DialogInterface.OnClickListener{ dialog, which -> })
                builder.show()
                val nickname = binding.registerActEtNickname.text.toString()
                binding.registerActEtNickname.visibility = View.GONE
                binding.registerActCheckBtn.visibility = View.GONE
                binding.registerActTvNickname.visibility = View.VISIBLE
                binding.registerActTvNickname.text = nickname
                binding.registerActCheckBtnSuccess.visibility = View.VISIBLE
            } else if (it is ApiState.Failure) {
                if (it.code == 999){
                    // ?????? ???????????? ?????????
                    Log.d(TAG, "?????? ???????????? ?????????")
                    val builder = AlertDialog.Builder(this@RegisterActivity)
                    builder.setMessage("?????? ???????????? ??????????????????")
                        .setPositiveButton("??????",
                            DialogInterface.OnClickListener{ dialog, which -> })
                    builder.show()
                } else {
                    Log.e(
                        TAG,
                        "observeNickname() code: ${it.code}"
                    )
                }
            } else if (it is ApiState.Init) {}
            else {
                Log.e(
                    TAG,
                    "?????? ?????? ??????"
                )
                val builder = AlertDialog.Builder(this@RegisterActivity)
                builder.setMessage("???????????? ?????? ?????? ??????")
                    .setPositiveButton("??????",
                        DialogInterface.OnClickListener{ dialog, which -> })
                builder.show()
            }
        }
    }

    private fun observeSave() = lifecycleScope.launchWhenStarted {
        viewModel.isSaveUserResult.collect {
            if (it is ApiState.Success) {
                Log.d(TAG, "?????? ?????? ??????")
                viewModel.userIdShared()
                viewModel.updateUserToken()
                startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
            } else if (it is ApiState.Failure) {
                if (it.code == 999) {
                    // ?????? ??????
                    Log.d(TAG, "?????? ?????? ??????")
                    val builder = AlertDialog.Builder(this@RegisterActivity)
                    builder.setMessage("?????? ?????? ??????")
                        .setPositiveButton("??????",
                            DialogInterface.OnClickListener{ dialog, which -> })
                    builder.show()
                } else {
                    Log.e(
                        TAG,
                        "observeSave() code: ${it.code}"
                    )
                }
            } else if (it is ApiState.Init) {
                Log.d(TAG, "observe ??????")
            } else {
                Log.e(
                    TAG,
                    "?????? ?????? ??????"
                )
                val builder = AlertDialog.Builder(this@RegisterActivity)
                builder.setMessage("???????????? ?????? ?????? ??????")
                    .setPositiveButton("??????",
                        DialogInterface.OnClickListener{ dialog, which -> })
                builder.show()
            }
        }
    }

}