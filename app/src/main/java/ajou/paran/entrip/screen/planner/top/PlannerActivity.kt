package ajou.paran.entrip.screen.planner.top

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityPlannerBinding
import ajou.paran.entrip.model.PlannerDate
import ajou.paran.entrip.model.fakeDateItemList
import ajou.paran.entrip.screen.planner.mid.MidFragment
import ajou.paran.entrip.screen.planner.top.useradd.PlannerUserAddActivity
import ajou.paran.entrip.util.hideKeyboard
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PlannerActivity: BaseActivity<ActivityPlannerBinding>(
    R.layout.activity_planner
), View.OnClickListener {
    companion object{
        const val TAG = "[PlannerActivity]"
    }

    private lateinit var dateRecyclerViewAdapter: DateRecyclerViewAdapter
    private lateinit var navController: NavController
    private lateinit var midFragment: MidFragment

    private val viewModel: PlannerActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        binding.plannerActEtTitle.setText(intent.getStringExtra("title") ?: "제목 없음")
        midFragment = MidFragment(
            // TODO date의 경우 db에 startDate 와 endDate 가 생기는 경우 변
            date = intent.getStringExtra("date") ?: fakeDateItemList[0].date,
            title = intent.getStringExtra("title") ?: binding.plannerActEtTitle.text.toString(),
            plannerId = "1"
        )
        if (savedInstanceState == null)
            setUpBottomNavigationBar()
        binding.plannerActEtTitle.setOnKeyListener { _, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // 타이틀 변경 이후 키보드 엔터 키를 입력하여 종료시 실행되는 부분
                Log.d(TAG, "Click Enter")
                binding.plannerActEtTitle.inputType = InputType.TYPE_NULL
                binding.plannerActEtTitle.isCursorVisible = false
                hideKeyboard()
                midFragment.setTitle(binding.plannerActEtTitle.text.toString())
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
                    startDateRangePicker()
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
        dateRecyclerViewAdapter = DateRecyclerViewAdapter(midFragment)
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
                binding.plannerActTvDate.text = it.first().date+" ~ "+it.last().date
            }
            else{
                binding.plannerActTvDate.text = it.first().date+" ~ "+it.last().date
                binding.plannerActRv1.visibility = View.GONE
            }
        })
    }

    private fun setUpBottomNavigationBar(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.plannerAct_nav_host_container, midFragment).commit()
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.plannerAct_nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController
//        navController.setGraph(R.navigation.nav_planner)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.plannerAct_bottom_nav)
        bottomNavigationView.setOnItemReselectedListener {
            when(it.itemId){
                R.id.nav_planner -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.plannerAct_nav_host_container, midFragment).commit()
                }
            }
        }
        bottomNavigationView.setupWithNavController(navController)

    }

    private fun showPlannerAddDeleteDialog() {
        val btnSheet = layoutInflater.inflate(R.layout.layout_bottom_sheet_planner_edit, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(btnSheet)
        btnSheet.findViewById<MaterialButton>(R.id.addBtn).setOnClickListener{
            Log.d(TAG, "Dialog.dismiss: addBtn")
            viewModel.plannerAdd()
            dialog.dismiss()
        }
        btnSheet.findViewById<MaterialButton>(R.id.deleteBtn).setOnClickListener{
            Log.d(TAG, "Dialog.dismiss: deleteBtn")
            viewModel.plannerDataDelete()
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("RestrictedApi")
    private fun startDateRangePicker() {
        val selector = CustomMaterialDatePicker()
        val dateRangePicker = MaterialDatePicker.Builder.customDatePicker(selector)
            .setTitleText("Select dates")
            .setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .build()
        dateRangePicker.show(supportFragmentManager,"Hello")

        dateRangePicker.addOnPositiveButtonClickListener { pairDate ->
            val format = SimpleDateFormat("yyyy/MM/dd",
                Locale.getDefault())
            val (s_year, s_month, s_day) = format.format(pairDate.first)
                .split("/")
                .map { it.toInt() }
            val (e_year, e_month, e_day) = format.format(pairDate.second)
                .split("/")
                .map { it.toInt() }

            val cal = Calendar.getInstance()
            cal.set(s_year, s_month, s_day)
            val mutableList = mutableListOf<PlannerDate>()
            while (cal.get(Calendar.YEAR) != e_year
                || cal.get(Calendar.MONTH) != e_month
                || cal.get(Calendar.DAY_OF_MONTH) != e_day
            ) {
                // 플래너에 집어 넣을 Date 생성 로직
                cal.add(Calendar.MONTH, -1)
                mutableList.add(
                    PlannerDate(format.format(cal.time))
                )
                cal.add(Calendar.MONTH, 1)
                cal.add(Calendar.DAY_OF_MONTH, 1)
            }
            cal.add(Calendar.MONTH, -1)
            mutableList.add(PlannerDate(format.format(cal.time)))
            viewModel.setPlannerDateItem(mutableList.toList())
            mutableList.clear()
            midFragment.setAdapter(format.format(pairDate.first))
        }
    }
}