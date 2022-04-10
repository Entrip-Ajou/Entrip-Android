package ajou.paran.entrip.screen.planner.mid

import ajou.paran.entrip.databinding.FragmentMidBinding
import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.screen.planner.mid.input.InputActivity
import ajou.paran.entrip.util.ui.SwipeHelperCallback
import ajou.paran.entrip.util.ui.VerticalSpaceItemDecoration
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MidFragment
constructor(
    private var date: String,
    private var plannerId: Long
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
        binding.rvPlan.addItemDecoration(VerticalSpaceItemDecoration(-8))
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
        }
        startActivity(intent)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setAdapter(date: String) {
        val planAdapter = PlanAdapter(this@MidFragment)
        val swipeHelperCallback = SwipeHelperCallback(planAdapter).apply {
            setClamp(resources.displayMetrics.widthPixels.toFloat()/4)
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.rvPlan)
        binding.rvPlan.setOnTouchListener{
            _,_ ->
            swipeHelperCallback.removePreviousClamp(binding.rvPlan)
            false
        }
        planAdapter.date = date
        planAdapter.plannerId = plannerId
        binding.rvPlan.adapter = planAdapter

        lifecycle.coroutineScope.launch {
            /*
            viewModel.date = date
            viewModel.plannerId = plannerId
             */
            viewModel.loadPlan(date, plannerId).collect() {
                planAdapter.submitList(it.toList())
            }
        }
    }

    fun getDate() = date

//    fun setTitle(title: String){
//        this.title = title
//         todo: title change event 필요
//    }
}