package ajou.paran.entrip.screen.planner.main


import ajou.paran.entrip.databinding.ActivityMainBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import ajou.paran.entrip.util.ApiState
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainAdapter.ItemClickListener{

    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    companion object {
        private const val TAG = "[MainActivity]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        observeState()
        setContentView(view)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView(){
        val mainAdapter = MainAdapter(this@MainActivity)
        binding.rvPlannerList.adapter = mainAdapter

        lifecycle.coroutineScope.launch{
            viewModel.selectAllPlanner()
                .onStart { viewModel.setLoading() }
                .catch { e ->
                    viewModel.hideLoading()
                }
                .collect {
                    viewModel.hideLoading()
                    mainAdapter.submitList(it.toList())
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
            binding.pbLoadingBar.visibility = View.VISIBLE
        }else{
            binding.pbLoadingBar.visibility = View.INVISIBLE
        }
    }

    private fun handleSuccess(data : Any){
        if(data is PlannerEntity){
            val intent = Intent(this, PlannerActivity::class.java)
            intent.putExtra("PlannerEntity", data)
            startActivity(intent)
            finish()
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
                val builder = AlertDialog.Builder(this)
                builder.setMessage("네트워크를 확인해주세요")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener{ dialog, which -> })
                builder.show()
            }

            500 -> {
                Toast.makeText(this,"다른 사용자에 의해 삭제된 플래너입니다.", Toast.LENGTH_LONG).show()
            }

            -1 -> {
                Log.e(TAG, "최상위 Exception class에서 예외 발생 -> 코드 로직 오류")
            }

            else -> {
                Log.e(TAG, "${code} Error handleError()에 추가 및 trouble shooting하기")
            }
        }
    }

    override fun onDeletePlannerClickListener(plannerEntity: PlannerEntity) {
        viewModel.deletePlanner(plannerEntity.planner_id)
    }

    override fun onPlannerClickListener(plannerEntity: PlannerEntity) {
        viewModel.selectPlanner(plannerEntity.planner_id)
    }

    override fun onPlannerAddClickListener() {
        // todo : 추후에 userId는 sharedpreference로 관리해서 넣기.
        viewModel.createPlanner("test1")
    }
}