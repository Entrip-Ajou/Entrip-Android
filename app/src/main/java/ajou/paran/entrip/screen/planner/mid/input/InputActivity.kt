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
    private val color_list = listOf(
        Color.parseColor("#cfc1c1"),
        Color.parseColor("#eca5a5"),
        Color.parseColor("#fbe1b5"),
        Color.parseColor("#e3e5a8"),
        Color.parseColor("#c6d9bf"),
        Color.parseColor("#c2e6e3"),
        Color.parseColor("#bcd6e8"),
        Color.parseColor("#d5d3ef")
    )
    private var palette_list = mutableListOf<ImageView>()
    private var last_select_palette: Int = 0

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
            viewModel.rgb.value = intent.getIntExtra("Rgb", Color.parseColor("#cfc1c1"))
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

        setUpColor()
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

                    500 -> {
                        Toast.makeText(this, "다른 사용자에 의해 삭제된 플래너입니다.", Toast.LENGTH_LONG).show()
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


    private fun setUpColor() {
        var drawable: GradientDrawable =
            ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
        drawable.setColor(color_list[0])
        binding.color.setImageDrawable(drawable)
        palette_list.add(binding.color)
        drawable = ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
        drawable.setColor(color_list[1])
        binding.color1.setImageDrawable(drawable)
        palette_list.add(binding.color1)
        drawable = ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
        drawable.setColor(color_list[2])
        binding.color2.setImageDrawable(drawable)
        palette_list.add(binding.color2)
        drawable = ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
        drawable.setColor(color_list[3])
        binding.color3.setImageDrawable(drawable)
        palette_list.add(binding.color3)
        drawable = ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
        drawable.setColor(color_list[4])
        binding.color4.setImageDrawable(drawable)
        palette_list.add(binding.color4)
        drawable = ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
        drawable.setColor(color_list[5])
        binding.color5.setImageDrawable(drawable)
        palette_list.add(binding.color5)
        drawable = ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
        drawable.setColor(color_list[6])
        binding.color6.setImageDrawable(drawable)
        palette_list.add(binding.color6)
        drawable = ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
        drawable.setColor(color_list[7])
        binding.color7.setImageDrawable(drawable)
        palette_list.add(binding.color7)
        initPaletteStroke()
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
                    this.putExtra("Rgb", viewModel.rgb.value)
                    this.putExtra("Time", timeToInt)
                    this.putExtra("Location", viewModel.location.value)
                    this.putExtra("date", viewModel.date)
                    this.putExtra("plannerId", viewModel.selectedPlanner.planner_id)
                    this.putExtra("PlannerEntity", viewModel.selectedPlanner)
                    this.putExtra("last_select_palette", last_select_palette)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    // 색상 선택
    fun selectPalette(v: View) {
        when (v.id) {
            binding.color.id -> {
                val drawable: GradientDrawable =
                    ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
                drawable.setColor(Color.parseColor("#cfc1c1"))
                last_select_palette = 0
                lockPaletteStroke(last_select_palette)
                binding.color.setImageDrawable(drawable)
                viewModel.rgb.value = Color.parseColor("#cfc1c1")
            }
            binding.color1.id -> {
                val drawable: GradientDrawable =
                    ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
                drawable.setColor(Color.parseColor("#eca5a5"))
                last_select_palette = 1
                lockPaletteStroke(last_select_palette)
                binding.color1.setImageDrawable(drawable)
                viewModel.rgb.value = Color.parseColor("#eca5a5")
            }
            binding.color2.id -> {
                val drawable: GradientDrawable =
                    ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
                drawable.setColor(Color.parseColor("#fbe1b5"))
                last_select_palette = 2
                lockPaletteStroke(last_select_palette)
                binding.color2.setImageDrawable(drawable)
                viewModel.rgb.value = Color.parseColor("#fbe1b5")
            }
            binding.color3.id -> {
                val drawable: GradientDrawable =
                    ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
                drawable.setColor(Color.parseColor("#e3e5a8"))
                last_select_palette = 3
                lockPaletteStroke(last_select_palette)
                binding.color3.setImageDrawable(drawable)
                viewModel.rgb.value = Color.parseColor("#e3e5a8")
            }
            binding.color4.id -> {
                val drawable: GradientDrawable =
                    ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
                drawable.setColor(Color.parseColor("#c6d9bf"))
                last_select_palette = 4
                lockPaletteStroke(last_select_palette)
                binding.color4.setImageDrawable(drawable)
                viewModel.rgb.value = Color.parseColor("#c6d9bf")
            }
            binding.color5.id -> {
                val drawable: GradientDrawable =
                    ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
                drawable.setColor(Color.parseColor("#c2e6e3"))
                last_select_palette = 5
                lockPaletteStroke(last_select_palette)
                binding.color5.setImageDrawable(drawable)
                viewModel.rgb.value = Color.parseColor("#c2e6e3")
            }
            binding.color6.id -> {
                val drawable: GradientDrawable =
                    ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
                drawable.setColor(Color.parseColor("#bcd6e8"))
                last_select_palette = 6
                lockPaletteStroke(last_select_palette)
                binding.color6.setImageDrawable(drawable)
                viewModel.rgb.value = Color.parseColor("#bcd6e8")
            }
            binding.color7.id -> {
                val drawable: GradientDrawable =
                    ContextCompat.getDrawable(this, R.drawable.circle_rgb_input) as GradientDrawable
                drawable.setColor(Color.parseColor("#d5d3ef"))
                last_select_palette = 7
                lockPaletteStroke(last_select_palette)
                binding.color7.setImageDrawable(drawable)
                viewModel.rgb.value = Color.parseColor("#d5d3ef")
            }
        }
    }

    private fun initPaletteStroke() {
        color_list.forEachIndexed { index, it ->
            if (it == viewModel.rgb.value) {
                (palette_list[index].drawable as GradientDrawable).setStroke(5, Color.parseColor("#0d82eb"))
            } else {
                (palette_list[index].drawable as GradientDrawable).setStroke(2, Color.parseColor("#000000"))
            }
        }
    }

    private fun lockPaletteStroke(index: Int) {
        palette_list.forEach {
            (it.drawable as GradientDrawable).setStroke(2, Color.parseColor("#000000"))
        }
        (palette_list[index].drawable as GradientDrawable).setStroke(5, Color.parseColor("#0d82eb"))
    }
}
