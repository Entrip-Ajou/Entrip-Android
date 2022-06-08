package ajou.paran.entrip.screen.planner.mid.comment

import ajou.paran.entrip.databinding.ActivityCommentBinding
import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.dto.CommentResponse
import ajou.paran.entrip.repository.network.dto.ResultSearchKeyword
import ajou.paran.entrip.screen.planner.main.MainPlannerAdapter
import ajou.paran.entrip.screen.planner.mid.map.SearchActivity
import ajou.paran.entrip.screen.planner.mid.map.SearchAdapter
import ajou.paran.entrip.screen.planner.mid.map.SearchAdapterDecoration
import ajou.paran.entrip.screen.planner.top.PlannerActivity
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
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CommentActivity : AppCompatActivity(), CommentAdapter.CommentItemClickListener{

    private lateinit var binding : ActivityCommentBinding
    private val viewModel : CommentViewModel by viewModels()
    private lateinit var selectedPlannner : PlannerEntity
    private lateinit var selectedPlan : PlanEntity

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val TAG = "[CommentActivity]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        val view = binding.root
        observeState()
        setContentView(view)
        setUpRecyclerView()
        init()
    }

    fun init(){
        selectedPlannner = intent.getParcelableExtra("PlannerEntity")!!
        selectedPlan = intent.getParcelableExtra("planEntity")!!

        /**
         *      상단 plan들 text 채우기
         */

        lifecycle.coroutineScope.launch {
            viewModel.selectComment(selectedPlan.id)
        }
    }

    private fun setUpRecyclerView() {
        val commentAdapter = CommentAdapter(this)
        binding.rvComment.adapter = commentAdapter
    }

    private fun observeState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach{ handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: ApiState) {
        when(state){
            is ApiState.Init -> Unit
            is ApiState.IsLoading -> handleLoading(state.isLoading)
            is ApiState.Success -> handleSuccess(state.data as List<CommentResponse>)
            is ApiState.Failure -> handleError(state.code)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbLoadingBar.visibility = View.VISIBLE
        } else {
            binding.pbLoadingBar.visibility = View.GONE
        }
    }

    private fun handleSuccess(data: List<CommentResponse>) {
        (binding.rvComment.adapter as CommentAdapter).submitList(data.toList())
    }

    private fun handleError(code : Int){
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
                Log.e(TAG, "code = ${code}")
            }
        }
    }


    fun onClick(v : View?){
        v?.let{
            when(it.id){
                binding.backButton.id -> {
                    val intent = Intent(this, PlannerActivity::class.java)
                    intent.putExtra("PlannerEntity", selectedPlannner)
                    startActivity(intent)
                    finish()
                }

                binding.tvEnter.id -> {
                    lifecycle.coroutineScope.launch{
                        if(!binding.etComment.text.isNullOrEmpty()){
                            val user_id = sharedPreferences.getString("user_id", null)!!
                            val content = binding.etComment.text.toString()
                            val plan_id = selectedPlan.id
                            viewModel.insertComment(user_id, content, plan_id)
                        }
                    }
                }

                else -> {}
            }
        }
    }

    override fun onCommentLongClickListener(commentResponse: CommentResponse) {
        if(sharedPreferences.getString("user_id", null).toString() == commentResponse.user_id){
            val builder = AlertDialog.Builder(this)
            builder.setMessage("삭제하시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener{ dialog, which ->
                        lifecycle.coroutineScope.launch(Dispatchers.IO){
                            viewModel.deleteComment(commentResponse.comment_id)
                        }
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener{ dialog, which -> }
                )
            builder.show()
        }
    }
}