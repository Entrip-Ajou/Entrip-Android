package ajou.paran.entrip.screen.planner.mid.input

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityInputBinding
import ajou.paran.entrip.screen.planner.mid.map.MapActivity
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_input.view.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

@AndroidEntryPoint
class InputActivity : BaseActivity<ActivityInputBinding>(
    R.layout.activity_input
) {

    companion object {
        private const val TAG = "[InputActivity]"
    }

    private val viewModel: InputViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        binding.inputViewModel = viewModel
        setUpView()
        observeState()
    }

    override fun onBackPressed() {
        // PlannerActivity가 MidFragment를 가지고 있으므로 변경
        val intent = Intent(this, PlannerActivity::class.java)
        intent.apply {
            this.putExtra("isFromInput", true)
            this.putExtra("date", viewModel.date)
            this.putExtra("PlannerEntity", viewModel.selectedPlanner)
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun setUpView() {
        val isUpdate = intent.getBooleanExtra("isUpdate", false)
        if (isUpdate) {
            viewModel.isUpdate = true
            viewModel.update_id = intent.getLongExtra("Id", -1)
            viewModel.todo.value = intent.getStringExtra("Todo")
            viewModel.location.value = intent.getStringExtra("Location")
            viewModel.date = intent.getStringExtra("date").toString()
            viewModel.planner_id = intent.getLongExtra("plannerId", -1)
            viewModel.selectedPlanner = intent.getParcelableExtra("PlannerEntity")!!
            val time = intent.getIntExtra("Time", -1)

            if (time == -1) {
                viewModel.time.value = null
            } else {
                val timeString = time.toString()
                var hour = ""
                var minute = ""

                when (timeString.length) {
                    1 -> {
                        hour = "00"
                        minute = "0" + timeString
                    }
                    2 -> {
                        hour = "00"
                        minute = timeString
                    }
                    3 -> {
                        hour = "0" + timeString.substring(0, 1)
                        minute = timeString.substring(timeString.length - 2, timeString.length)
                    }
                    4 -> {
                        hour = timeString.substring(0, 2)
                        minute = timeString.substring(timeString.length - 2, timeString.length)
                    }
                }
                viewModel.time.value = "$hour:$minute"
            }
        } else {
            viewModel.date = intent.getStringExtra("date").toString()
            viewModel.planner_id = intent.getLongExtra("plannerId", -1)
            viewModel.selectedPlanner = intent.getParcelableExtra("PlannerEntity")!!
        }
    }

    private fun observeState() {
        viewModel.inputState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: InputState) {
        when (state) {
            is InputState.Init -> Unit
            is InputState.Empty -> {
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

            is InputState.IsLoading -> {
                if (state.isLoading) binding.loadingBar.visibility = View.VISIBLE
                else binding.loadingBar.visibility = View.INVISIBLE
            }

            is InputState.Success -> {
                // PlannerActivity가 MidFragment를 가지고 있으므로 변경
                onBackPressed()
            }

            is InputState.Failure -> {
                when (state.code) {
                    0 -> {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("네트워크를 확인해주세요")
                            .setPositiveButton("확인",
                                DialogInterface.OnClickListener { dialog, which -> })
                        builder.show()
                    }

                    -1 -> {
                        Log.e(TAG, "최상위 Exception class에서 예외 발생 -> 코드 로직 오류")
                    }

                    else -> {
                        Log.e(TAG, "${state.code} Error handleError()에 추가 및 trouble shooting하기")
                    }
                }
            }
        }
    }


    // backButton, time, location 클릭 이벤트 처리
    fun input_click(v: View) {
        when (v.id) {
            binding.backButton.id -> {
                // PlannerActivity가 MidFragment를 가지고 있으므로 변경
                onBackPressed()
            }

            binding.tvTime.id -> {
                val currentTime = Calendar.getInstance()
                val startHour = currentTime.get(Calendar.HOUR_OF_DAY)
                val startMinute = currentTime.get(Calendar.MINUTE)

                TimePickerDialog(
                    this@InputActivity,
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        if (minute < 10) {
                            binding.tvTime.text = "$hourOfDay:0$minute"
                        } else {
                            binding.tvTime.text = "$hourOfDay:$minute"
                        }
                    },
                    startHour,
                    startMinute,
                    true
                ).show()
            }

            binding.tvLocation.id -> {
                var timeToInt: Int = -1
                if (!viewModel.time.value.isNullOrEmpty()) {
                    val timeArray = viewModel.time.value!!.split(":")
                    val timeToString = timeArray[0].plus(timeArray[1])
                    timeToInt = Integer.parseInt(timeToString)
                }

                val intent = Intent(this, MapActivity::class.java)
                intent.apply {
                    this.putExtra("isUpdate", true)
                    this.putExtra("Id", viewModel.update_id)
                    this.putExtra("Todo", viewModel.todo.value)
                    this.putExtra("Time", timeToInt)
                    this.putExtra("Location", viewModel.location.value)
                    this.putExtra("date", viewModel.date)
                    this.putExtra("plannerId", viewModel.selectedPlanner.planner_id)
                    this.putExtra("PlannerEntity", viewModel.selectedPlanner)
                }
                startActivity(intent)
            }
        }
    }
}
