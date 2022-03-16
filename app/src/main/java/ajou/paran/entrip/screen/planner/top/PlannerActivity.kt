package ajou.paran.entrip.screen.planner.top

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityPlannerBinding
import ajou.paran.entrip.screen.planner.top.useradd.PlannerUserAddActivity
import ajou.paran.entrip.util.hideKeyboard
import android.content.Intent
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlannerActivity: BaseActivity<ActivityPlannerBinding>(
    R.layout.activity_planner
), View.OnClickListener {
    companion object{
        const val TAG = "[PlannerActivity]"
    }

    private lateinit var dateRecyclerViewAdapter: DateRecyclerViewAdapter
    private lateinit var navController: NavController


    private val viewModel: PlannerActivityViewModel by viewModels()


    override fun init() {
        setUpBottomNavigationBar()
        binding.plannerActEtTitle.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // 타이틀 변경 이후 키보드 엔터 키를 입력하여 종료시 실행되는 부분
                Log.d(TAG, "Click Enter")
                binding.plannerActEtTitle.inputType = InputType.TYPE_NULL
                binding.plannerActEtTitle.isCursorVisible = false
                hideKeyboard()
                true
            } else {
                false
            }
        }
        initDateRecyclerView()
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlannerDateItem()
    }

    /**
     * @funcName: onClick
     * @func: View Click Event
     * @Case
     *  - Close
     *  - Click planner title button
     *  - Change planner title text
     *  - Edit planner Date
     *  - Add planner
     *  - Add user
     * @Date: 2022.03.08
     * @Made: Jeon
     * **/
    override fun onClick(view: View?) {
        view?.let {
            when(it.id){
                binding.plannerActIvClose.id -> {
                    Log.d(TAG, "Case: Close")
                    onBackPressed()
                }
                binding.plannerActEtTitle.id -> {
                    Log.d(TAG, "Case: Click planner title button")
                    binding.plannerActEtTitle.inputType = InputType.TYPE_CLASS_TEXT
                }
                binding.plannerActTvDate.id, binding.plannerActIvDateEdit.id -> {
                    Log.d(TAG, "Case: Edit planner Date")
                    viewModel.startDateRangePicker(supportFragmentManager)
                }
                binding.plannerActIvPlannerAdd.id -> {
                    Log.d(TAG, "Case: Add planner")
                    showPlannerAddDeleteDialog()
                }
                binding.plannerActPersonAdd.id -> {
                    Log.d(TAG, "Case: Add user")
                    startActivity(Intent(this, PlannerUserAddActivity::class.java))
                }
                else -> {
                    return
                }
            }
        }
    }

    /**
     * @funcName: initDateRecyclerView
     * @func: Recycler Setting(Adapter, LayoutManager)
     * @Date: 2022.03.08
     * @Made: Jeon
     * **/
    private fun initDateRecyclerView() = binding.plannerActRv1.apply {
        dateRecyclerViewAdapter = DateRecyclerViewAdapter()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = dateRecyclerViewAdapter
    }

    /**
     * @funcName: subscribeObservers
     * @func: observe
     * @Date: 2022.03.08
     * @Made: Jeon
     * **/
    private fun subscribeObservers() {
        viewModel.plannerDateItemList.observe(this, Observer {
            if(it.first() != it.last()){
                if (binding.plannerActRv1.visibility == View.GONE)
                    binding.plannerActRv1.visibility = View.VISIBLE
                dateRecyclerViewAdapter.submitList(it)
                binding.plannerActTvDate.text = "${it.first().month}/${it.first().day} ~ ${it.last().month}/${it.last().day}"
            }
            else{
                binding.plannerActTvDate.text = "${it.first().month}/${it.first().day}"
                binding.plannerActRv1.visibility = View.GONE
            }
        })
    }


    private fun setUpBottomNavigationBar(){
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.plannerAct_nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.plannerAct_bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

    }

    private fun showPlannerAddDeleteDialog() {
        val btnsheet = layoutInflater.inflate(R.layout.layout_bottom_sheet_planner_edit, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnsheet)
        btnsheet.findViewById<MaterialButton>(R.id.addBtn).setOnClickListener{
            Log.d(TAG, "Dialog.dismiss: addBtn")
            dialog.dismiss()
        }
        btnsheet.findViewById<MaterialButton>(R.id.deleteBtn).setOnClickListener{
            Log.d(TAG, "Dialog.dismiss: deleteBtn")
            dialog.dismiss()
        }
        dialog.show()
    }
}