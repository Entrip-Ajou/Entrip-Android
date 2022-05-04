package ajou.paran.entrip.screen.planner.top

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityPlannerBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.screen.planner.mid.MidFragment
import ajou.paran.entrip.screen.planner.top.useradd.PlannerUserAddActivity
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.ui.hideKeyboard
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.util.Pair
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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

    private lateinit var selectedPlanner : PlannerEntity

    private val viewModel: PlannerActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        selectedPlanner = intent.getParcelableExtra("PlannerEntity")!!

        midFragment = MidFragment(
            date = selectedPlanner.start_date,
            plannerId = selectedPlanner.planner_id
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
                viewModel.plannerChange(binding.plannerActEtTitle.text.toString(), selectedPlanner)
                true
            } else {
                false
            }
        }
        observeState()
        initDateRecyclerView()
        subscribeObservers()
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
                binding.plannerActIvDateEdit.id,
                binding.plannerActTvStartDate.id,
                binding.plannerActTvEndDate.id -> {
                    Log.d(TAG, "Case: Edit planner Date")
                    startDateRangePicker()
                }
                binding.plannerActIvPlannerAdd.id -> {
                    Log.d(TAG, "Case: Add planner")
                    viewModel.createPlanner("test1")
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

    private fun subscribeObservers() {
        lifecycle.coroutineScope.launch {
            viewModel.getFlowPlanner(selectedPlanner.planner_id).collect {
                val list = getDates(it.start_date, it.end_date)
                binding.plannerActEtTitle.setText(it.title)
                if(it.start_date != it.end_date){
                    if (binding.plannerActRv1.visibility == View.GONE)
                        binding.plannerActRv1.visibility = View.VISIBLE
                    binding.plannerActTvStartDate.text = it.start_date
                    binding.plannerActTvEndDate.text = it.end_date
                }
                else{
                    binding.plannerActTvStartDate.text = it.start_date
                    binding.plannerActTvEndDate.text = it.end_date
                    binding.plannerActRv1.visibility = View.GONE
                }
                dateRecyclerViewAdapter.submitList(list)
            }
        }
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

            val mutableList = getDates(
                start_date = "$s_year/$s_month/$s_day",
                end_date = "$e_year/$e_month/$e_day"
            )
            viewModel.plannerChange(mutableList.toList(), selectedPlanner)
            midFragment.setAdapter(format.format(pairDate.first))
        }
    }

    private fun getDates(start_date: String, end_date: String): List<PlannerDate>{
        val format = SimpleDateFormat("yyyy/MM/dd",
            Locale.getDefault())

        val (s_year, s_month, s_day) = start_date
            .split("/")
            .map { it.toInt() }
        val (e_year, e_month, e_day) = end_date
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

        return mutableList
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
            binding.plannerLoadingBar.visibility = View.VISIBLE
        }else{
            binding.plannerLoadingBar.visibility = View.INVISIBLE
        }
    }

    private fun handleSuccess(data : Any){
        if(data is PlannerEntity){
            val intent = Intent(baseContext, PlannerActivity::class.java)
            intent.putExtra("PlannerEntity", data)
            startActivity(intent)
        }
    }

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
}