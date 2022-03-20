package ajou.paran.entrip.screen.planner.mid


import ajou.paran.entrip.databinding.FragmentMidBinding
import ajou.paran.entrip.model.PlanEntity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MidFragment : Fragment(),PlanAdapter.RowClickListener {

    companion object {
        private const val TAG = "[MidFragment]"
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

        val planAdapter = PlanAdapter(this@MidFragment)
        binding.rvPlan.adapter = planAdapter
        lifecycle.coroutineScope.launch {
            viewModel.loadPlan().collect() {
                planAdapter.submitList(it.toList())
            }
        }
    }

    override fun onDeletePlanClickListener(planEntity: PlanEntity) {
        viewModel.deletePlan(planEntity)
    }

    override fun onItemClickListener(planEntity: PlanEntity) {
        // todo : 위에서 delete를 한뒤 선택하면 update가 안되어있음.
        Log.e(TAG, "해당 아이템 클릭")
        Log.e(TAG, "Todo : " + planEntity.todo)
        Log.e(TAG, "Time : " + planEntity.time)
        Log.e(TAG, "rgb : " + planEntity.rgb)
    }
}