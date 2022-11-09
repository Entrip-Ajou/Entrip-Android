package ajou.paran.entrip.screen.planner.top.vote.find

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityVoteFindBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.dto.UsersAndContentsReturnDto
import ajou.paran.entrip.repository.network.dto.VotesFullInfoReturnDto
import ajou.paran.entrip.screen.planner.top.notice.NoticeActivity
import ajou.paran.entrip.screen.planner.top.vote.VoteActivity
import ajou.paran.entrip.util.ApiState
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class VoteFindActivity : BaseActivity<ActivityVoteFindBinding>(
    R.layout.activity_vote_find
) {
    companion object {
        private const val TAG = "[VoteFindAct]"
    }

    private val viewModel : VoteFindViewModel by viewModels()
    private lateinit var selectedPlanner: PlannerEntity
    private var vote_id = -1L
    private lateinit var user_id : String
    private var hostSameUser = false
    private var contentsAndUserList = mutableListOf<UsersAndContentsReturnDto>()

    private var toggleFlag = false

    override fun init(savedInstanceState: Bundle?) {
        selectedPlanner = intent.getParcelableExtra("PlannerEntity")!!
        vote_id = intent.getLongExtra("vote_id", -1)
        user_id = intent.getStringExtra("user_id")!!
        hostSameUser = intent.getBooleanExtra("hostSameUser", false)

        if(hostSameUser) binding.btnTerminate.visibility = View.VISIBLE
        else binding.btnTerminate.visibility = View.GONE

        setupVotingRecyclerView()
        viewModel.getVotes(vote_id)
        observeState()
    }

    private fun setupVotingRecyclerView(){
        binding.rvVote.apply {
            adapter = VoteFindAdapter(mutableListOf())
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
            binding.votePostLoadingBar.visibility = View.VISIBLE
        } else {
            binding.votePostLoadingBar.visibility = View.GONE
        }
    }

    private fun handleSuccess(data: Any) {
        when (data) {
            is List<*> -> {
                (data as List<Long>)?.forEach { t ->
                    binding.rvVote.adapter?.let{ a ->
                        (a as VoteFindAdapter).selectedList[a.mappingIndex(t).toInt()] = true
                    }
                }

                if(data.size > 0){
                    binding.btnVotePress.text = "다시 투표하기"
                    toggleFlag = true
                    binding.rvVote.adapter?.let { a ->
                        (a as VoteFindAdapter).notifyDataSetChanged()
                    }
                }

            }

            is VotesFullInfoReturnDto -> {
                contentsAndUserList = mutableListOf<UsersAndContentsReturnDto>()
                data.contentsAndUsers.forEach { t ->
                    contentsAndUserList.add(t)
                }
                binding.rvVote.adapter?.let{ a ->
                    (a as VoteFindAdapter).update(contentsAndUserList)
                    a.configuration(data.anonymousVote, data.multipleVotes, data.voting)
                }

                if(!data.voting){
                    binding.btnVotePress.isClickable = false
                    binding.btnTerminate.isClickable = false

                    binding.btnVotePress.setBackgroundColor(Color.GRAY)
                    binding.btnTerminate.setBackgroundColor(Color.GRAY)
                }
                viewModel.getPreviousVotes(user_id, vote_id)
            }

            is Long -> onBackPressed()

            is Unit -> {}

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
                binding.btnBackToVote.id -> {
                    onBackPressed()
                }

                binding.btnVotePress.id -> {
                    var contentsList = mutableListOf<Long>()
                    binding.rvVote.adapter?.let{ a ->
                        (a as VoteFindAdapter).selectedList.forEachIndexed { index, value ->
                            if(value){
                                contentsList.add(contentsAndUserList.get(index).contentId)
                            }
                        }
                    }

                    if(toggleFlag) {
                        viewModel.undoVote(vote_id, contentsList, user_id)
                    }

                    viewModel.doVote(vote_id, contentsList, user_id)
                }

                binding.btnTerminate.id -> {
                    viewModel.terminate(vote_id)
                }

                else -> {}
            }
        }
    }

    override fun onBackPressed() {
        Log.i(TAG, "onBackPressed() 호출")
        super.onBackPressed()
        val intent = Intent(this, VoteActivity::class.java)
        intent.putExtra("PlannerEntity", selectedPlanner)
        startActivity(intent)
        finish()
    }

}