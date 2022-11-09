package ajou.paran.entrip.screen.planner.top.notice

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityNoticeBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.dto.*
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import ajou.paran.entrip.screen.planner.top.notice.find.NoticeFindActivity
import ajou.paran.entrip.screen.planner.top.notice.post.NoticePostActivity
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
class NoticeActivity : BaseActivity<ActivityNoticeBinding>(
    R.layout.activity_notice
), NoticeAdapter.RowClickListener {

    companion object {
        private const val TAG = "[NoticeActivity]"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var planner_id: Long = -1L
    private val viewModel: NoticeViewModel by viewModels()
    private lateinit var selectedPlanner: PlannerEntity
    private var noticeList = mutableListOf<NoticeResponse>()

    override fun init(savedInstanceState: Bundle?) {
        binding.noticeViewModel = viewModel
        selectedPlanner = intent.getParcelableExtra("PlannerEntity")!!
        planner_id = selectedPlanner.planner_id
        binding.noticePlannerTitle.text = selectedPlanner.title

        observeState()
        setupNoticeRecyclerView()

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.noticeLoadingBar.visibility = View.GONE
            viewModel.fetchAllNotices(planner_id)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observeState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun setupNoticeRecyclerView() {
        binding.rvNotice.apply {
            val user_id = sharedPreferences.getString("user_id", null).toString()
            adapter = NoticeAdapter(mutableListOf(), this@NoticeActivity, user_id)
        }
        viewModel.fetchAllNotices(planner_id)
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
            is Unit -> {
                binding.rvNotice.adapter?.let { a ->
                    (a as NoticeAdapter).update(noticeList)
                }
            }

            is List<*> -> {
                binding.rvNotice.adapter?.let { a ->
                    noticeList.clear()
                    (data as List<NoticeResponse>)?.forEach { t ->
                        noticeList.add(t)
                    }
                    (a as NoticeAdapter).update(noticeList)
                }
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
            when (it.id) {
                binding.btnBackToPlanner.id -> {
                    onBackPressed()
                }

                binding.btnPostNotice.id -> {
                    val intent = Intent(this, NoticePostActivity::class.java)
                    intent.putExtra("PlannerEntity", selectedPlanner)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        Log.i(TAG, "onBackPressed() 호출")
        super.onBackPressed()
        val intent = Intent(this, PlannerActivity::class.java)
        intent.putExtra("PlannerEntity", selectedPlanner)
        startActivity(intent)
        finish()
    }

    override fun onItemClickListener(noticeResponse: NoticeResponse) {
        val intent = Intent(this, NoticeFindActivity::class.java)
        intent.putExtra("PlannerEntity", selectedPlanner)
        intent.putExtra("notice_id", noticeResponse.notice_id)
        startActivity(intent)
        finish()
    }

    override fun onUpdateItemClickListener(noticeResponse: NoticeResponse) {
        val intent = Intent(this, NoticePostActivity::class.java)
        intent.putExtra("PlannerEntity", selectedPlanner)
        intent.putExtra("isUpdate", true)
        intent.putExtra("noticeResponse", noticeResponse)
        startActivity(intent)
        finish()
    }

    override fun onDeleteItemClickListener(noticeResponse: NoticeResponse) {
        viewModel.deleteNotice(noticeResponse.notice_id)
    }
}