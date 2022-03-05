package ajou.paran.entrip.screen.activity

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityPlannerBinding
import ajou.paran.entrip.screen.adapter.DateRecyclerViewAdapter
import ajou.paran.entrip.screen.viewmodel.PlannerActivityViewModel
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlannerActivity: BaseActivity<ActivityPlannerBinding>(
    R.layout.activity_planner,
    { ActivityPlannerBinding.inflate(it) }
), View.OnClickListener {
    companion object{
        const val TAG = "PlannerActivity"
    }

    private lateinit var dateRecyclerViewAdapter: DateRecyclerViewAdapter
    private val viewModel: PlannerActivityViewModel by viewModels()

    override fun init() {

        // insert onClickListener
        binding.plannerActIvClose.setOnClickListener(this)
        binding.plannerActTvTitle.setOnClickListener(this)
        binding.plannerActIvTitleEdit.setOnClickListener(this)
        binding.plannerActTvDate.setOnClickListener(this)
        binding.plannerActIvDateEdit.setOnClickListener(this)
        binding.plannerActIvPlannerAdd.setOnClickListener(this)
        binding.plannerActPersonAdd.setOnClickListener(this)

        initDateRecyclerView()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlannerDateItem()
    }

    override fun onClick(view: View?) {
        view?.let {
            when(it.id){
                binding.plannerActIvClose.id -> {
                    Log.d(TAG, "Case: Close")
                }
                binding.plannerActTvTitle.id, binding.plannerActIvTitleEdit.id -> {
                    Log.d(TAG, "Case: Edit planner title")
                }
                binding.plannerActTvDate.id, binding.plannerActIvDateEdit.id -> {
                    Log.d(TAG, "Case: Edit planner Date")
                }
                binding.plannerActIvPlannerAdd.id -> {
                    Log.d(TAG, "Case: Planner Add")
                }
                binding.plannerActPersonAdd.id -> {
                    Log.d(TAG, "Case: User Add")
                }
                else -> {
                    return
                }
            }
        }
    }

    private fun initDateRecyclerView() = binding.plannerActRv1.apply {
        dateRecyclerViewAdapter = DateRecyclerViewAdapter()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = dateRecyclerViewAdapter
    }

    private fun subscribeObservers() {
        viewModel.plannerDateItemList.observe(this, Observer { dateRecyclerViewAdapter.submitList(it) })
    }

}