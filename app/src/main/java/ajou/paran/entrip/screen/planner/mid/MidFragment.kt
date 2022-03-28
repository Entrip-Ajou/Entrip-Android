package ajou.paran.entrip.screen.planner.mid

import ajou.paran.entrip.databinding.FragmentMidBinding
import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.screen.planner.mid.input.InputActivity
import android.content.Intent
import android.os.Bundle
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
class MidFragment
constructor(
    private var date: String,
    private var title: String,
    private var plannerId: String
): Fragment(),PlanAdapter.RowClickListener {
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

        /*
        Todo : Intent로 Date RecyclerView의 default date를 넘겨주세요
        1. MidFragment를 담고 있는 Activity에서 date, plannerId를 이쪽으로 넘김
        2. date, planner_id를 추출하여 변수 만들어주시고
        3. MidViewModel에도 넣어주세요(lateinit var date, planner_id 만들어놨습니다. 할당해주심 될거같아요)
        4. 코루틴 안에 loadPlan 매개변수에 넣어주세요
        */

        setAdapter(date)
    }

    override fun onDeletePlanClickListener(planEntity: PlanEntity) {
        viewModel.deletePlan(planEntity)
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
        }
        startActivity(intent)
    }

    fun setAdapter(date: String) {
        val planAdapter = PlanAdapter(this@MidFragment)
        planAdapter.date = date
        planAdapter.title = title
        planAdapter.plannerId = plannerId
        binding.rvPlan.adapter = planAdapter

        lifecycle.coroutineScope.launch {
            viewModel.date = date
            viewModel.plannerId = plannerId
            viewModel.loadPlan(date, plannerId).collect() {
                planAdapter.submitList(it.toList())
            }
        }
    }

    fun getDate() = date

    fun setTitle(title: String){
        this.title = title
        // todo: title change event 필요
    }
}