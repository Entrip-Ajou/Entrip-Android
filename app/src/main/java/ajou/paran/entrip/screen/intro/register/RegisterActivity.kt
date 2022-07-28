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
                        when (binding.radioGroup.checkedRadioButtonId) {
                            binding.registerActRadioMan.id -> {
                                viewModel.saveUserResult(0, binding.registerActTvNickname.text.toString())
                                observeSave()
                            }
                            binding.registerActRadioWoman.id -> {
                                viewModel.saveUserResult(1, binding.registerActTvNickname.text.toString())
                                observeSave()
                            }
                            -1 -> {
                                binding.registerActRadioMan.setBackgroundResource(R.drawable.shape_register_error)
                                binding.registerActRadioWoman.setBackgroundResource(R.drawable.shape_register_error)
                            }
                            else -> {
                                Log.d(TAG, "성별이 더 들어갈 경우 들어옴")
                            }
                        }
                    } else {
                        Log.d(TAG, "중복체크 실패")
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
            when(it) {
                is ApiState.Success -> {
                    Log.d(TAG, "존재하지 않는 닉네임")
                    endCondition = true
                    val builder = AlertDialog.Builder(this@RegisterActivity)
                    builder.setMessage("존재하지 않는 닉네임입니다")
                        .setPositiveButton("확인",
                            DialogInterface.OnClickListener{ dialog, which -> })
                    builder.show()
                    val nickname = binding.registerActEtNickname.text.toString()
                    binding.registerActEtNickname.visibility = View.GONE
                    binding.registerActCheckBtn.visibility = View.GONE
                    binding.registerActTvNickname.visibility = View.VISIBLE
                    binding.registerActTvNickname.text = nickname
                    binding.registerActCheckBtnSuccess.visibility = View.VISIBLE
                }
                is ApiState.Failure -> {
                    when(it.code) {
                        999 -> {
                            // 이미 존재하는 아이디
                            Log.d(TAG, "이미 존재하는 닉네임")
                            val builder = AlertDialog.Builder(this@RegisterActivity)
                            builder.setMessage("이미 존재하는 닉네임입니다")
                                .setPositiveButton("확인",
                                    DialogInterface.OnClickListener{ dialog, which -> })
                            builder.show()
                        }
                        else -> { Log.e(TAG, "observeNickname() code: ${it.code}") }
                    }
                }
                is ApiState.Init -> { Log.d(TAG, "observeNickname() Init") }
                else -> {
                    Log.e(TAG, "예상 못한 에러")
                    val builder = AlertDialog.Builder(this@RegisterActivity)
                    builder.setMessage("예상하지 못한 오류 발생")
                        .setPositiveButton("확인") { dialog, which -> dialog.dismiss() }
                    builder.show()
                }
            }
        }
    }

    private fun observeSave() = lifecycleScope.launchWhenStarted {
        viewModel.isSaveUserResult.collect {
            when(it) {
                is ApiState.Success -> {
                    Log.d(TAG, "유저 저장 성공")
                    viewModel.userIdShared()
                    viewModel.updateUserToken()
                    startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                }
                is ApiState.Failure -> {
                    when(it.code) {
                        999 -> {
                            // 저장 실패
                            Log.d(TAG, "유저 저장 실패")
                            val builder = AlertDialog.Builder(this@RegisterActivity)
                            builder.setMessage("유저 저장 실패")
                                .setPositiveButton("확인") { dialog, which -> dialog.dismiss() }
                            builder.show()
                        }
                        else -> {
                            Log.e(TAG, "observeSave() code: ${it.code}")
                        }
                    }
                }
                is ApiState.Init -> {
                    Log.d(TAG, "observe 시작")
                }
                else -> {
                    Log.e(TAG, "예상 못한 에러")
                    val builder = AlertDialog.Builder(this@RegisterActivity)
                    builder.setMessage("예상하지 못한 오류 발생")
                        .setPositiveButton("확인") { dialog, which -> dialog.dismiss() }
                    builder.show()
                }
            }
        }
    }

}