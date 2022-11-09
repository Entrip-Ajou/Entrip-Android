package ajou.paran.entrip.screen.planner.top.notice.find

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityNoticeBinding
import ajou.paran.entrip.databinding.ActivityNoticeFindBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.dto.NoticeResponse
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import ajou.paran.entrip.screen.planner.top.notice.NoticeActivity
import ajou.paran.entrip.screen.planner.top.notice.NoticeAdapter
import ajou.paran.entrip.screen.planner.top.notice.NoticeViewModel
import ajou.paran.entrip.screen.planner.top.notice.post.NoticePostActivity
import ajou.paran.entrip.util.ApiState
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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

@AndroidEntryPoint
class NoticeFindActivity : BaseActivity<ActivityNoticeFindBinding>(
    R.layout.activity_notice_find
) {
    companion object {
        private const val TAG = "[NoticeFindAct]"
    }

    private val viewModel : NoticeFindViewModel by viewModels()
    private lateinit var selectedPlanner: PlannerEntity
    private var notice_id = -1L

    override fun init(savedInstanceState: Bundle?) {
        binding.noticeFindViewModel = viewModel
        selectedPlanner = intent.getParcelableExtra("PlannerEntity")!!
        notice_id = intent.getLongExtra("notice_id", -1)
        observeState()

        if(notice_id != -1L){
            viewModel.findNotice(notice_id)
        }else{
            val builder = AlertDialog.Builder(this)
            builder.setMessage("다시 시도해주세요")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss()})
            builder.show()
            onBackPressed()
        }
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
            binding.noticeLoadingBar.visibility = View.VISIBLE
        } else {
            binding.noticeLoadingBar.visibility = View.GONE
        }
    }

    private fun handleSuccess(data: Any) {
        when (data) {
            is NoticeResponse -> {
                binding.noticeTvTitle.text = data.title
                binding.noticeTvContent.text = data.content
            }

            else -> {
                Log.e(TAG, "성공했으나 Data type이 잘못됨")
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
            }
        }
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