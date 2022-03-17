package ajou.paran.entrip.screen.planner.mid.input

import ajou.paran.entrip.MainActivity
import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityInputBinding
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class InputActivity : BaseActivity<ActivityInputBinding>(
    R.layout.activity_input
) {

    companion object {
        private const val TAG = "[InputActivity]"
    }

    private val viewModel: InputViewModel by viewModels()

    override fun init() {
        binding.inputViewModel = viewModel
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.inputState.observe(this) {
            when (it) {
                is InputState.initialized -> {
                    binding.todoStar.visibility = View.VISIBLE
                    binding.todoNotNull.visibility = View.VISIBLE
                    binding.timeStar.visibility = View.VISIBLE
                    binding.timeNotNull.visibility = View.VISIBLE
                }

                is InputState.NoTodo -> {
                    binding.todoStar.visibility = View.VISIBLE
                    binding.todoNotNull.visibility = View.VISIBLE
                    binding.timeStar.visibility = View.INVISIBLE
                    binding.timeNotNull.visibility = View.INVISIBLE
                }

                is InputState.NoTime -> {
                    binding.todoStar.visibility = View.INVISIBLE
                    binding.todoNotNull.visibility = View.INVISIBLE
                    binding.timeStar.visibility = View.VISIBLE
                    binding.timeNotNull.visibility = View.VISIBLE
                }

                is InputState.Success -> {
                    Log.d(TAG, "SUCCESS")
                }
            }
        }
    }

    // backButton, time, location 클릭 이벤트 처리
    fun input_click(v: View) {
        when (v.id) {
            binding.backButton.id -> {
                /* Activity -> Activity 인 이유는, 현재는 MainActivity가 MidFragment를 가지고 있기 때문에,
                추후에 희훈님 코드랑 합칠 때, midFragment를 가지고 있는 activity로 변경해야 함.
                 */
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            binding.tvTime.id -> {
                val currentTime = Calendar.getInstance()
                val startHour = currentTime.get(Calendar.HOUR_OF_DAY)
                val startMinute = currentTime.get(Calendar.MINUTE)

                TimePickerDialog(
                    this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        if (minute < 10) {
                            binding.tvTime.setText("$hourOfDay : 0$minute")
                        } else {
                            binding.tvTime.setText("$hourOfDay : $minute")
                        }
                    },
                    startHour, startMinute, true
                ).show()
            }

            binding.tvLocation.id -> {

            }
        }
    }

    // 색상 선택
    fun selectPalette(v: View) {
        when (v.id) {
            binding.color.id -> {
                binding.etTodo.setBackgroundColor(Color.WHITE)
                viewModel.rgb.value = Color.WHITE
            }
            binding.color1.id -> {
                binding.etTodo.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.red
                    )
                )
                viewModel.rgb.value = R.color.red
            }
            binding.color2.id -> {
                binding.etTodo.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.orange
                    )
                )
                viewModel.rgb.value = R.color.orange
            }
            binding.color3.id -> {
                binding.etTodo.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.yellow
                    )
                )
                viewModel.rgb.value = R.color.yellow
            }
            binding.color4.id -> {
                binding.etTodo.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.green
                    )
                )
                viewModel.rgb.value = R.color.green
            }
            binding.color5.id -> {
                binding.etTodo.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.blue
                    )
                )
                viewModel.rgb.value = R.color.blue
            }
            binding.color6.id -> {
                binding.etTodo.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.indigo
                    )
                )
                viewModel.rgb.value = R.color.indigo
            }
            binding.color7.id -> {
                binding.etTodo.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.purple
                    )
                )
                viewModel.rgb.value = R.color.purple
            }
        }
    }
}
