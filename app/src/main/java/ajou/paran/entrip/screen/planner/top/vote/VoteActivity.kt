package ajou.paran.entrip.screen.planner.top.vote

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityNoticeBinding
import ajou.paran.entrip.databinding.ActivityVoteBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.dto.NoticeResponse
import ajou.paran.entrip.repository.network.dto.VoteResponse
import ajou.paran.entrip.repository.network.dto.VotesSaveRequestDto
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import ajou.paran.entrip.screen.planner.top.notice.NoticeActivity
import ajou.paran.entrip.screen.planner.top.notice.NoticeAdapter
import ajou.paran.entrip.screen.planner.top.notice.NoticeViewModel
import ajou.paran.entrip.screen.planner.top.notice.post.NoticePostActivity
import ajou.paran.entrip.screen.planner.top.vote.find.VoteFindActivity
import ajou.paran.entrip.screen.planner.top.vote.post.VotePostActivity
import ajou.paran.entrip.util.ApiState
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
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
import javax.inject.Inject

@AndroidEntryPoint
class VoteActivity : BaseActivity<ActivityVoteBinding>(
    R.layout.activity_vote
), VotingAdapter.RowClickListener {
    companion object {
        private const val TAG = "[VoteActivity]"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var planner_id : Long = -1L
    private val viewModel : VoteViewModel by viewModels()
    private lateinit var selectedPlanner: PlannerEntity
    private lateinit var user_id : String

    override fun init(savedInstanceState: Bundle?) {
        binding.voteViewModel = viewModel
        selectedPlanner = intent.getParcelableExtra("PlannerEntity")!!
        planner_id = selectedPlanner.planner_id
        user_id = sharedPreferences.getString("user_id", null).toString()
        Log.e(TAG, "user_id = " + user_id)
        binding.votePlannerTitle.text = selectedPlanner.title

        observeState()
        viewModel.fetchVotes(planner_id)
        setupVotingRecyclerView()
        setupVoteFinishRecyclerView()

        binding.swipeRefreshLayout.setOnRefreshListener{
            binding.voteLoadingBar.visibility = View.GONE
            viewModel.fetchVotes(planner_id)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupVotingRecyclerView(){
        binding.rvVoting.apply {
            adapter = VotingAdapter(mutableListOf(), this@VoteActivity, user_id)
        }
    }

    private fun setupVoteFinishRecyclerView(){
        binding.rvVoteFinish.apply {
            adapter = VoteFinishAdapter(mutableListOf(), this@VoteActivity)
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
            binding.voteLoadingBar.visibility = View.VISIBLE
        } else {
            binding.voteLoadingBar.visibility = View.GONE
        }
    }

    private fun handleSuccess(data: Any) {
        when (data) {
            is List<*> -> {
                var votingList = mutableListOf<VoteResponse>()
                var finishList = mutableListOf<VoteResponse>()

                (data as List<VoteResponse>)?.forEach { t ->
                    if(t.voting) votingList.add(t)
                    else finishList.add(t)
                }

                binding.rvVoting.adapter?.let{ a ->
                    (a as VotingAdapter).update(votingList)
                }

                binding.rvVoteFinish.adapter?.let{ a ->
                    (a as VoteFinishAdapter).update(finishList)
                }
            }

            is Unit -> {
                viewModel.fetchVotes(planner_id)
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

                binding.btnPostVote.id -> {
                    val intent = Intent(this, VotePostActivity::class.java)
                    intent.putExtra("PlannerEntity", selectedPlanner)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, PlannerActivity::class.java)
        intent.putExtra("PlannerEntity", selectedPlanner)
        startActivity(intent)
        finish()
    }

    override fun onItemClickListener(voteResponse: VoteResponse) {
        val intent = Intent(this, VoteFindActivity::class.java)
        intent.putExtra("PlannerEntity", selectedPlanner)
        intent.putExtra("vote_id", voteResponse.vote_id)
        intent.putExtra("user_id", user_id)
        if(voteResponse.host_id == user_id) intent.putExtra("hostSameUser", true)
        else intent.putExtra("hostSameUser", false)
        startActivity(intent)
        finish()
    }

    override fun onUpdateItemClickListener(voteResponse: VoteResponse) {
        // 추후에 다시 개발
    }

    override fun onDeleteItemClickListener(voteResponse: VoteResponse) {
        viewModel.deleteVotes(voteResponse.vote_id)
    }
}