package ajou.paran.entrip.screen.planner.top.notice.post

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityNoticePostBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.dto.NoticeResponse
import ajou.paran.entrip.repository.network.dto.NoticesSaveRequest
import ajou.paran.entrip.repository.network.dto.NoticesUpdateRequest
import ajou.paran.entrip.screen.planner.top.notice.NoticeActivity
import ajou.paran.entrip.screen.planner.top.notice.NoticeViewModel
import ajou.paran.entrip.util.ApiState
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class NoticePostActivity : BaseActivity<ActivityNoticePostBinding>(
    R.layout.activity_notice_post
) {

    companion object {
        private const val TAG = "[NoticePostAct]"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val viewModel: NoticePostViewModel by viewModels()
    private lateinit var selectedPlanner : PlannerEntity
    private lateinit var noticeResponse : NoticeResponse
    private var isUpdate = false

    override fun init(savedInstanceState: Bundle?) {
        selectedPlanner = intent.getParcelableExtra("PlannerEntity")!!
        isUpdate = intent.getBooleanExtra("isUpdate", false)
        if(isUpdate){
            noticeResponse = intent.getParcelableExtra("noticeResponse")!!
            binding.noticeEtTitle.setText(noticeResponse.title)
            binding.noticeEtContent.setText(noticeResponse.content)
        }
        observeState()
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
            binding.noticePostLoadingBar.visibility = View.VISIBLE
        } else {
            binding.noticePostLoadingBar.visibility = View.GONE
        }
    }

    private fun handleSuccess(data: Any) {
        when (data) {
            is Unit -> {
                onBackPressed()
            }
            else -> {
                Log.e(TAG, "이상한데로 들어옴")
            }
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
            when (it.id) {
                binding.btnBackToNotice.id -> {
                    onBackPressed()
                }

                binding.btnPostNotice.id -> {
                    if(checkNull()) {
                        if(isUpdate){
                            val builder = AlertDialog.Builder(this)
                            builder.setMessage("수정하시겠습니까?")
                                .setPositiveButton("확인",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        val noticesUpdateRequest = NoticesUpdateRequest(
                                            title = binding.noticeEtTitle.text.toString(),
                                            content = binding.noticeEtContent.text.toString()
                                        )
                                        viewModel.updateNotice(noticeResponse.notice_id, noticesUpdateRequest)
                                    })
                                .setNegativeButton("취소",
                                    DialogInterface.OnClickListener { dialog, which -> })
                            builder.show()
                        }else {
                            val noticesSaveRequest = NoticesSaveRequest(
                                author = sharedPreferences.getString("user_id", null)!!,
                                title = binding.noticeEtTitle.text.toString(),
                                content = binding.noticeEtContent.text.toString(),
                                planner_id = selectedPlanner.planner_id
                            )
                            viewModel.saveNotice(noticesSaveRequest)
                        }
                    }
                }
            }
        }
    }

    private fun checkNull() : Boolean {
        var result = true
        if(binding.noticeEtTitle.text.isNullOrEmpty()){
            binding.titleStar.visibility = View.VISIBLE
            binding.titleNotNull.visibility = View.VISIBLE
            result = false
        }

        if(binding.noticeEtContent.text.isNullOrEmpty()){
            binding.contentStar.visibility = View.VISIBLE
            binding.contentNotNull.visibility = View.VISIBLE
            result = false
        }


        return result
    }

    override fun onBackPressed() {
        Log.i(TAG, "onBackPressed() 호출")
        super.onBackPressed()
        val intent = Intent(this, NoticeActivity::class.java)
        intent.putExtra("PlannerEntity", selectedPlanner)
        startActivity(intent)
        finish()
    }

}