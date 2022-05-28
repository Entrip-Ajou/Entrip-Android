package ajou.paran.entrip.screen.planner.mid

import ajou.paran.entrip.databinding.FragmentMidBinding
import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.screen.planner.mid.input.InputActivity
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.ui.SwipeHelperCallback
import ajou.paran.entrip.util.ui.VerticalSpaceItemDecoration
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MidFragment: Fragment(),PlanAdapter.RowClickListener {
    companion object {
        private const val TAG = "[MidFragment]"

        private lateinit var selectedPlanner : PlannerEntity
        private lateinit var date: String
        private var plannerId: Long = -1L

        fun newInstance(aDate: String, aPlannerId: Long, aSelectedPlanner: PlannerEntity): MidFragment{
            val fragment = MidFragment()
            date = aDate
            plannerId = aPlannerId
            selectedPlanner = aSelectedPlanner

            return fragment
        }

    }

    private val viewModel: MidViewModel by viewModels()
    private lateinit var binding: FragmentMidBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMidBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.midViewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPlan.addItemDecoration(VerticalSpaceItemDecoration(-8))
        observeState()
        setAdapter(date)
    }

    override fun onDeletePlanClickListener(planEntity: PlanEntity) {
        viewModel.deletePlan(planEntity.id, planEntity.planner_idFK)
    }

    override fun onItemClickListener(planEntity: PlanEntity) {
        val intent = Intent(context, InputActivity::class.java)
        intent.apply {
            this.putExtra("isUpdate", true)
            this.putExtra("Id", planEntity.id)
            this.putExtra("Todo",planEntity.todo)
            this.putExtra("Rgb",planEntity.rgb)
            this.putExtra("Time",planEntity.time)
            this.putExtra("Location",planEntity.location)
            this.putExtra("date", planEntity.date)
            this.putExtra("PlannerEntity", selectedPlanner)
        }
        startActivity(intent)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setAdapter(date: String) {
        val planAdapter = PlanAdapter(this@MidFragment)
        val swipeHelperCallback = SwipeHelperCallback(planAdapter).apply {
            setClamp(resources.displayMetrics.widthPixels.toFloat()/4)
//            setClamp(200f)
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.rvPlan)
        binding.rvPlan.setOnTouchListener{
            _,_ ->
            swipeHelperCallback.removePreviousClamp(binding.rvPlan)
            false
        }
        planAdapter.date = date
        planAdapter.plannerId = plannerId
        planAdapter.selectedPlanner = selectedPlanner
        binding.rvPlan.adapter = planAdapter

        lifecycle.coroutineScope.launch {
            viewModel.loadPlan(date, plannerId)
                .onStart { viewModel.setLoading() }
                .catch { e ->
                    viewModel.hideLoading()
                }
                .collect {
                    viewModel.hideLoading()
                    planAdapter.submitList(it.toList())
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
            is ApiState.Success -> Unit
            is ApiState.Failure -> handleError(state.code)
        }
    }

    private fun handleError(code : Int){
        when(code){
            0 -> {
                val builder = AlertDialog.Builder(activity!!)
                builder.setMessage("네트워크를 확인해주세요")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener{ dialog, which -> })
                builder.show()
            }

            500 -> {
                Toast.makeText(activity,"다른 사용자에 의해 삭제된 플래너입니다.", Toast.LENGTH_LONG).show()
            }

            -1 -> {
                Log.e(TAG, "최상위 Exception class에서 예외 발생 -> 코드 로직 오류")
            }

            else -> {
                Log.e(TAG, "${code} Error handleError()에 추가 및 trouble shooting하기")
            }
        }
    }

    private fun handleLoading(isLoading : Boolean){
        if(isLoading){
            binding.loadingBar.visibility = View.VISIBLE
        }else{
            binding.loadingBar.visibility = View.INVISIBLE
        }
    }
}