package ajou.paran.entrip.screen.planner.top.vote.post

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityVotePostBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.dto.NoticesUpdateRequest
import ajou.paran.entrip.repository.network.dto.VotesSaveRequestDto
import ajou.paran.entrip.screen.planner.top.notice.post.NoticePostViewModel
import ajou.paran.entrip.screen.planner.top.vote.VoteActivity
import ajou.paran.entrip.util.ApiState
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextClock
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_vote_post.*
import kotlinx.android.synthetic.main.date_time_picker.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class VotePostActivity : BaseActivity<ActivityVotePostBinding>(
    R.layout.activity_vote_post
) {
    companion object {
        private const val TAG = "[VotePostAct]"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val viewModel: VotePostViewModel by viewModels()
    private lateinit var selectedPlanner : PlannerEntity
    private lateinit var user_id : String
    private var deadLine : String? = null

    private var radio1Toggle = false
    private var radio2Toggle = false
    private var radio3Toggle = false


    private lateinit var editTextGroup : Array<EditText>
    private var et_count = 3

    override fun init(savedInstanceState: Bundle?) {
        selectedPlanner = intent.getParcelableExtra("PlannerEntity")!!
        user_id = sharedPreferences.getString("user_id", null).toString()
        observeState()
        initEditGroup()
    }

    private fun initEditGroup(){
        editTextGroup = arrayOf(
            binding.etContent1,
            binding.etContent2,
            binding.etContent3,
            binding.etContent4,
            binding.etContent5,
            binding.etContent6,
            binding.etContent7,
            binding.etContent8,
            binding.etContent9,
            binding.etContent10
        )
    }

    private fun observeState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: ApiState) {
        when (state) {
            is ApiState.Init -> Unit
            is ApiState.IsLoading -> handleLoading(state.isLoading)
            is ApiState.Success -> handleSuccess(state.data)
            is ApiState.Failure -> handleError(state.code)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.votePostLoadingBar.visibility = View.VISIBLE
        } else {
            binding.votePostLoadingBar.visibility = View.GONE
        }
    }

    private fun handleSuccess(data: Any) {
        when (data) {
            is Unit -> {
                onBackPressed()
            }
            else -> {}
        }
    }

    private fun handleError(code: Int) {
        when (code) {
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
                Log.e(TAG, "${code} Error handleError()에 추가 및 trouble shooting하기")
            }
        }
    }

    fun onClick(view: View?) {
        view?.let {
            when(it.id) {
                binding.btnBackToVote.id -> {
                    onBackPressed()
                }

                binding.btnAdd.id -> {
                    if(et_count <= 10) {
                        editTextGroup[et_count-1].visibility = View.VISIBLE
                        et_count++
                    } else {}
                }

                binding.radio1.id -> {
                    radio1Toggle = !radio1Toggle
                    binding.radio1.isChecked = radio1Toggle
                }

                binding.radio2.id -> {
                    radio2Toggle = !radio2Toggle
                    binding.radio2.isChecked = radio2Toggle
                }

                binding.radio3.id -> {
                    radio3Toggle = !radio3Toggle
                    binding.radio3.isChecked = radio3Toggle
                    if(binding.radio3.isChecked){
                        var selected_date : String = ""
                        var selected_time : String = ""
                        val c = Calendar.getInstance()

                        val mDialogView = LayoutInflater.from(this).inflate(R.layout.date_time_picker, null)
                        val mBuilder = AlertDialog.Builder(this)
                            .setView(mDialogView)
                            .setTitle("마감기한 설정")

                        val mAlertDialog = mBuilder.show()

                        val date_picker_active = mDialogView.findViewById<TextView>(R.id.date_picker)
                        date_picker_active.setOnClickListener {
                            val year = c.get(Calendar.YEAR)
                            val month = c.get(Calendar.MONTH)
                            val day = c.get(Calendar.DAY_OF_MONTH)

                            val datePickerDialog = DatePickerDialog(
                                this,
                                { view, year, monthOfYear, dayOfMonth ->
                                    selected_date = "" + year + '-' + (monthOfYear+1) + "-" + dayOfMonth
                                    date_picker_active.text = selected_date
                                },
                                year,
                                month,
                                day
                            )
                            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
                            datePickerDialog.show()
                        }

                        val time_picker_activie = mDialogView.findViewById<TextView>(R.id.time_picker)
                        time_picker_activie.setOnClickListener {
                            val time_picker_active = mDialogView.findViewById<TextView>(R.id.time_picker)
                            time_picker_activie.setOnClickListener {
                                val selectedHour = c.get(Calendar.HOUR_OF_DAY)
                                val selectedMinute = c.get(Calendar.MINUTE)

                                val timePickerDialog = TimePickerDialog(
                                    this,
                                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                        var hourOfDayToString = hourOfDay.toString()
                                        var minuteToString = minute.toString()

                                        if(hourOfDay < 10) hourOfDayToString = "0" + hourOfDayToString
                                        if(minute < 10) minuteToString = "0"+minuteToString

                                        selected_time = "" + hourOfDayToString + ":" + minuteToString
                                        time_picker_active.text = selected_time
                                    },
                                    selectedHour,
                                    selectedMinute,
                                    true
                                )

                                timePickerDialog.show()
                            }
                        }

                        val btn_ok = mDialogView.findViewById<Button>(R.id.btn_confirm)
                        btn_ok.setOnClickListener {
                            if(!selected_time.isNullOrEmpty() && !selected_date.isNullOrEmpty()){
                                deadLine = selected_date + " " + selected_time
                                mAlertDialog.dismiss()
                            } else{
                                val builder = AlertDialog.Builder(this)
                                builder.setMessage("날짜 및 시간을 입력해주세요.")
                                    .setPositiveButton("확인",
                                        DialogInterface.OnClickListener { dialog, which -> })
                                builder.show()
                            }
                        }

                        val btn_cancel = mDialogView.findViewById<Button>(R.id.btn_cancel)
                        btn_cancel.setOnClickListener {
                            // 변수 다시 초기화
                            mAlertDialog.dismiss()
                        }
                    }else { deadLine = null }
                }

                binding.btnPostVote.id -> {
                    if(checkNull()){
                        var contentsList = mutableListOf<String>()
                        for(i in 0 until 10){
                            if(!editTextGroup[i].text.isNullOrEmpty()) contentsList.add(editTextGroup[i].text.toString())
                        }
                        Log.e(TAG, "deadLine = " + deadLine)

                        val voteSaveRequest = VotesSaveRequestDto(
                            title = binding.etTitle.text.toString(),
                            contents = contentsList,
                            multipleVotes = binding.radio1.isChecked,
                            anonymousVotes = binding.radio2.isChecked,
                            deadLine = deadLine,
                            planner_id = selectedPlanner.planner_id,
                            author = user_id
                        )
                        viewModel.saveVote(voteSaveRequest)
                    }else{
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("제목과 항목 2개 이상 입력해주세요")
                            .setPositiveButton("확인",
                                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss()})
                            .show()
                    }

                }
                else -> { }
            }
        }
    }

    private fun checkNull() : Boolean {
        if(binding.etTitle.text.isNullOrEmpty()) return false
        var count = 0
        for(i in 0 until 10){
            if(!editTextGroup[i].text.isNullOrEmpty()) count++;
        }
        if(count < 2) return false
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, VoteActivity::class.java)
        intent.putExtra("PlannerEntity", selectedPlanner)
        startActivity(intent)
        finish()
    }

}