package ajou.paran.entrip.screen.home

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentHomeBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.model.test.nullRecommenItem
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import ajou.paran.entrip.screen.recommendation.RecommendationFragment
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.ui.RecyclerViewDecoration
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>(R.layout.fragment_home),
    HomePlannerAdapter.ItemClickListener {
    companion object{
        const val TAG = "[HomeFragment]"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: HomeFragmentViewModel by viewModels()

    private lateinit var recommendItemAdapter: HomeRecommendAdapter

    override fun init() {
        binding.homeFragBtnRecommendation.setOnClickListener{
            (activity as HomeActivity).changeFrag(RecommendationFragment())
        }
        observeState()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView(){
        val plannerAdapter = HomePlannerAdapter(this)
        binding.homeFragRvPlanner.adapter = plannerAdapter
        binding.homeFragRvPlanner.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.homeFragRvPlanner.addItemDecoration(RecyclerViewDecoration(30))

        recommendItemAdapter = HomeRecommendAdapter()
        binding.homeFragRvRecommendation.adapter = recommendItemAdapter
        recommendItemAdapter.submitList(nullRecommenItem)
        binding.homeFragRvRecommendation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.homeFragRvRecommendation.addItemDecoration(RecyclerViewDecoration(30))

        lifecycle.coroutineScope.launch{
            viewModel.selectAllPlanner()
                .onStart { viewModel.setLoading() }
                .catch { viewModel.hideLoading() }
                .collect {
                    viewModel.hideLoading()
                    plannerAdapter.submitList(it.toList())
                    binding.homeFragRvPlanner.scrollToPosition(0)
                }
        }
    }

    private fun observeState(){
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach{ handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state : ApiState){
        when(state){
            is ApiState.Init -> Unit
            is ApiState.IsLoading -> handleLoading(state.isLoading)
            is ApiState.Success -> handleSuccess(state.data)
            is ApiState.Failure -> handleError(state.code)
        }
    }

    private fun handleLoading(isLoading : Boolean){
        if(isLoading){
            binding.homeFragPbLoadingBar.visibility = View.VISIBLE
        }else{
            binding.homeFragPbLoadingBar.visibility = View.GONE
        }
    }

    private fun handleSuccess(data : Any){
        when(data) {
            is PlannerEntity -> {
                val intent = Intent(context, PlannerActivity::class.java)
                intent.putExtra("PlannerEntity", data)
                startActivity(intent)
            }
            else -> {  }
        }
    }

    /**
     *    <<< 개발 과정에서 추가적인 Error 발생 시 이쪽에 추가하기 >>>
     *    0 -> NoInternetException
     *    -1 -> Exception
     *    500 -> Internal Server Error
     *
     */
    private fun handleError(code : Int){
        when(code){
            0 -> {
                val builder = AlertDialog.Builder(context!!)
                builder.setMessage("네트워크를 확인해주세요")
                    .setPositiveButton("확인") { dialog, which -> dialog.dismiss() }
                builder.show()
            }

            500 -> {
                Toast.makeText(context,"다른 사용자에 의해 삭제된 플래너입니다.", Toast.LENGTH_LONG).show()
            }

            -1 -> {
                Log.e(TAG, "최상위 Exception class에서 예외 발생 -> 코드 로직 오류")
            }

            else -> {
                Log.e(TAG, "$code Error handleError()에 추가 및 trouble shooting하기")
            }
        }
    }

    override fun onPlannerClickListener(plannerEntity: PlannerEntity) {
        val intent = Intent(context, PlannerActivity::class.java)
        intent.putExtra("PlannerEntity", plannerEntity)
        startActivity(intent)
    }

    override fun onPlannerAddClickListener() {
        viewModel.createPlanner(sharedPreferences.getString("user_id", null).toString())
    }

}