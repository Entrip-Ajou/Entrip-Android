package ajou.paran.entrip.screen.planner.top

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityPlannerBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.screen.home.HomeActivity
import ajou.paran.entrip.screen.planner.mid.MidFragment
import ajou.paran.entrip.screen.planner.top.notice.NoticeActivity
import ajou.paran.entrip.screen.planner.top.useradd.PlannerUserAddActivity
import ajou.paran.entrip.screen.planner.top.vote.VoteActivity
import ajou.paran.entrip.util.ui.hideKeyboard
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.util.Pair
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PlannerActivity: BaseActivity<ActivityPlannerBinding>(
    R.layout.activity_planner
), View.OnClickListener {
    companion object{
        const val TAG = "[PlannerAct]"
    }

    private lateinit var dateRecyclerViewAdapter: DateRecyclerViewAdapter
    private lateinit var midFragment: MidFragment

    private lateinit var selectedPlanner : PlannerEntity

    private lateinit var init_start_date : String
    private lateinit var init_end_date : String

    private val viewModel: PlannerActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        selectedPlanner = intent.getParcelableExtra("PlannerEntity")!!
        init_start_date = selectedPlanner.start_date
        init_end_date = selectedPlanner.end_date

        /**
         *  Case 1) Planner List -> Planner Activity
         *          - selectedPlanner를 intent로 넘겨준다.
         *
         *  Case 2) InputActivity -> Planner Activity
         *          - date를 intent로 넘겨준다.
         *
         *  Case 3) PlannerAddActivity -> PlannerActivity
         *          - selectedPlanner를 intent로 넘겨준다.
         */
        // Case 2에 대한 flag
        var isFromInput = intent.getBooleanExtra("isFromInput", false)
        val date : String
        if(isFromInput)
            date = intent.getStringExtra("date")!!
        else
            date = selectedPlanner.start_date

        midFragment = MidFragment.newInstance(date, selectedPlanner.planner_id, selectedPlanner)

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
                selectedPlanner
                true
            } else {
                false
            }
        }
        observeState()
        viewModel.fetchRemoteDB(selectedPlanner.planner_id)
        initDateRecyclerView(date)
        subscribeObservers()
        viewModel.runStomp(selectedPlanner.planner_id)
    }

    override fun onClick(view: View?) {
        view?.let {
            when(it.id){
                binding.plannerActIvClose.id -> {
                    onBackPressed()
                }
                binding.plannerActEtTitle.id -> {
                    Log.d(TAG, "Case: Click planner title button")
                    binding.plannerActEtTitle.inputType = InputType.TYPE_CLASS_TEXT
                }
                binding.plannerActClDate.id,
                binding.plannerActIvDateEdit.id,
                binding.plannerActTvStartDate.id,
                binding.plannerActTvEndDate.id -> {
                    Log.d(TAG, "Case: Edit planner Date")
                    startDateRangePicker()
                }

                binding.plannerActIvPlannerExit.id -> {
                    // dialog
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("플래너를 삭제하시겠습니까?")
                        .setPositiveButton("확인",
                            DialogInterface.OnClickListener { dialog, which ->
                                viewModel.deletePlanner(selectedPlanner.planner_id)
                            })
                        .setNegativeButton("취소",
                            DialogInterface.OnClickListener { dialog, which ->

                            })
                    builder.show()
                }

                binding.plannerActMenu.id -> {
                    val popup = PopupMenu(this, binding.plannerActMenu)
                    menuInflater.inflate(R.menu.navigation_menu, popup.menu)

                    popup.setOnMenuItemClickListener { item ->
                        when(item.itemId){
                            R.id.gallery -> Log.e(TAG, "Gallery 선택")
                            R.id.info -> {
                                val intent = Intent(this, NoticeActivity::class.java)
                                intent.putExtra("PlannerEntity", selectedPlanner)
                                startActivity(intent)
                            }
                            R.id.vote -> {
                                val intent = Intent(this, VoteActivity::class.java)
                                intent.putExtra("PlannerEntity", selectedPlanner)
                                startActivity(intent)
                            }
                            R.id.invite -> {
                                val intent = Intent(this, PlannerUserAddActivity::class.java)
                                intent.putExtra("PlannerEntity", selectedPlanner)
                                startActivity(intent)
                            }
                        }
                        false
                    }
                    popup.show()
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
    private fun initDateRecyclerView(date : String) = binding.plannerActRv1.apply {
        dateRecyclerViewAdapter = DateRecyclerViewAdapter(midFragment)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = dateRecyclerViewAdapter
    }

    private fun subscribeObservers()
    = lifecycleScope.launchWhenResumed {
        viewModel.getFlowPlanner(selectedPlanner.planner_id).collect {
            selectedPlanner = it
            val list = getDates(it.start_date, it.end_date)
            binding.plannerActEtTitle.setText(it.title)
            when(it.start_date) {
                it.end_date -> {
                    binding.plannerActRv1.visibility = View.GONE
                }
                else -> {
                    binding.plannerActRv1.visibility = View.VISIBLE
                }
            }
            binding.plannerActTvStartDate.text = it.start_date
            binding.plannerActTvEndDate.text = it.end_date
            dateRecyclerViewAdapter.submitList(list)
        }
    }

    private fun setUpBottomNavigationBar(){
        binding.plannerActBottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    intent.run {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        putExtra("last_pos", R.id.nav_home)
                    }
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_planner -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.plannerAct_nav_host_container, midFragment).commit()
                    true
                }
                R.id.nav_recommendation -> {
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    intent.run {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        putExtra("last_pos", R.id.nav_recommendation)
                    }
                    startActivity(intent)
                    finish()
                    true
                }
//                R.id.nav_board -> {
//                    val intent = Intent(applicationContext, HomeActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    intent.putExtra("last_pos", R.id.nav_board)
//                    startActivity(intent)
//                    finish()
//                    true
//                }
                else -> false
            }
        }

        binding.plannerActBottomNav.selectedItemId = R.id.nav_planner

    }

    @SuppressLint("RestrictedApi")
    private fun startDateRangePicker() {
        val selector = CustomMaterialDatePicker()

        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val formattedStartDate = formatter.parse(init_start_date)
        val formattedEndDate = formatter.parse(init_end_date)

        val dateRangePicker = MaterialDatePicker.Builder.customDatePicker(selector)
            .setTitleText("Select dates")
            .setSelection(
                Pair(
                    formattedStartDate.time,
                    formattedEndDate.time
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

            val list = getDates(
                start_date = "$s_year/$s_month/$s_day",
                end_date = "$e_year/$e_month/$e_day"
            )
            viewModel.plannerChange(list, selectedPlanner)
            init_start_date = "$s_year/$s_month/$s_day"
            init_end_date = "$e_year/$e_month/$e_day"
            midFragment.setAdapter(format.format(pairDate.first))
        }
    }

    private fun getDates(start_date: String, end_date: String): List<PlannerDate>{
        val format = SimpleDateFormat("yyyy/MM/dd",
            Locale.getDefault())

        val (s_year, s_month, s_day) = start_date
            .split("/")
            .map { it.toInt() }

        val time = Calendar.getInstance()
        time.set(s_year,s_month, s_day)
        val t = kotlin.math.abs(format.parse(start_date).time - format.parse(end_date).time)
        val dates = t / (24 * 60 * 60 * 1000)
        var i = -1
        val mutableList = mutableListOf<PlannerDate>()

        // time 한달 더해져있는 상태로 나와서 한달 빼기
        time.add(Calendar.MONTH, -1)

        while (i < dates){
            mutableList.add(
                PlannerDate(format.format(time.time))
            )
            time.add(Calendar.DAY_OF_MONTH, 1)
            i += 1
        }
        return mutableList.toList()
    }

    private fun observeState(){
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach{ handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state : PlannerState){
        when(state){
            is PlannerState.Init -> Unit
            is PlannerState.IsLoading -> handleLoading(state.isLoading)
            is PlannerState.Success -> handleSuccess(state.data)
            is PlannerState.Failure -> handleError(state.code)
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
        when (data) {
            is Boolean -> {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            is Unit -> {

            }
            else -> {
                Log.e(TAG, "State = Success<"+data.javaClass.toString()+ ">Type의 처리 필요")
            }
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

            -1 -> {
                Log.e(TAG, "최상위 Exception class에서 예외 발생 -> 코드 로직 오류")
            }

            else -> {
                Log.e(TAG, "${code} Error handleError()에 추가 및 trouble shooting하기")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d(TAG, "Case: Close")
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("isFromPlanner",true)
        intent.putExtra("last_pos", R.id.nav_planner)
        startActivity(intent)
        finish()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        viewModel.stompClient.disconnect()
    }
}