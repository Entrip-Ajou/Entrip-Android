package ajou.paran.entrip.screen.planner.mid.map

import ajou.paran.entrip.databinding.ActivitySearchBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.dto.Place
import ajou.paran.entrip.repository.network.dto.ResultSearchKeyword
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.ui.hideKeyboard
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchBinding
    private val viewModel : SearchViewModel by viewModels()

    /**
     *  intent data들
     */
    private var isUpdate : Boolean = false
    private var update_id : Long = -1L
    private var todo : String? = null
    private var location : String? = null
    private var rgb : Int = -1
    private lateinit var date : String
    private var planner_id : Long = -1L
    private lateinit var selectedPlanner : PlannerEntity
    private var time : Int = -1
    private var last_select_palette : Int = 0

    companion object {
        private const val TAG = "[SearchActivity]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        observeState()
        setContentView(view)
        setUpRecyclerView()
        getIntentData()
        binding.etSearchKeyword.setOnKeyListener { _, keyCode, event ->
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER){
                Log.d(TAG, "Enter Click")
                if(!binding.etSearchKeyword.text.isNullOrEmpty()){
                    val keyword = binding.etSearchKeyword.text.toString()
                    binding.tvValidation.visibility = View.INVISIBLE
                    hideKeyboard()
                    viewModel.searchKeyword(keyword)
                }
                true
            }else{
                false
            }
        }
    }

    private fun observeState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach{ handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: ApiState) {
        when(state){
            is ApiState.Init -> Unit
            is ApiState.IsLoading -> handleLoading(state.isLoading)
            is ApiState.Success -> handleSuccess(state.data as ResultSearchKeyword)
            is ApiState.Failure -> handleError(state.code)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbLoadingBar.visibility = View.VISIBLE
        } else {
            binding.pbLoadingBar.visibility = View.GONE
        }
    }

    private fun handleSuccess(data: ResultSearchKeyword) {
        binding.rvSearchList.adapter?.let{ a ->

            if(data.documents.size == 0){
                binding.tvIsNotFound.visibility = View.VISIBLE
            }else{
                binding.tvIsNotFound.visibility = View.GONE
            }

            (a as SearchAdapter).update(data.documents)
        }
        binding.rvSearchList.addItemDecoration(SearchAdapterDecoration())
    }

    private fun handleError(code : Int){
        when (code) {
            0 -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("네트워크를 확인해주세요")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, which -> })
                builder.show()
            }

            -1 -> {
                Log.e(TAG, "최상위 Exception class에서 예외 발생 -> 코드 로직 오류")
            }

            else -> {
                Log.e(TAG, "code = ${code}")
            }
        }
    }


    fun onClick(v : View?){
        v?.let{
            when(it.id){
                binding.imgAddressSearch.id -> {
                    val keyword = binding.etSearchKeyword.text.toString()
                    if(!keyword.isNullOrBlank()){
                        binding.tvValidation.visibility = View.INVISIBLE
                        hideKeyboard()
                        viewModel.searchKeyword(keyword)
                    }else{
                        binding.tvValidation.visibility = View.VISIBLE
                        Log.e(TAG, "keyword = null")
                    }
                }

                binding.imgBackMap.id -> {
                    val intent = Intent(this, MapActivity::class.java)
                    intent.apply{
                        this.putExtra("isUpdate", isUpdate)
                        this.putExtra("Id", update_id)
                        this.putExtra("Todo",todo)
                        this.putExtra("Rgb",rgb)
                        this.putExtra("Time",time)
                        this.putExtra("Location",location)
                        this.putExtra("date", date)
                        this.putExtra("plannerId", planner_id)
                        this.putExtra("PlannerEntity", selectedPlanner)
                        this.putExtra("last_select_palette",last_select_palette)
                    }
                    startActivity(intent)
                    finish()
                }

                else -> {}
            }
        }
    }

    private fun setUpRecyclerView() {
        var adapter = SearchAdapter(mutableListOf())
        binding.rvSearchList.adapter = adapter
        adapter.setOnItemClickListener(object : SearchAdapter.RowCLickListener{
            override fun onItemClickListener(place: Place) {
                val intent = Intent(applicationContext, MapActivity::class.java)
                intent.apply {
                    this.putExtra("isSearch", true)
                    this.putExtra("place_name",place.place_name)
                    this.putExtra("road_address_name",place.road_address_name)
                    this.putExtra("longitude",place.x)
                    this.putExtra("latitude",place.y)

                    this.putExtra("isUpdate", isUpdate)
                    this.putExtra("Id", update_id)
                    this.putExtra("Todo",todo)
                    this.putExtra("Rgb",rgb)
                    this.putExtra("Time",time)
                    this.putExtra("Location",location)
                    this.putExtra("date", date)
                    this.putExtra("plannerId", planner_id)
                    this.putExtra("PlannerEntity", selectedPlanner)
                    this.putExtra("last_select_palette",last_select_palette)
                }
                startActivity(intent)
                finish()
            }
        })
        binding.rvSearchList.layoutManager = LinearLayoutManager(this@SearchActivity)
    }

    /**
     *    InputActivity로 돌아갈때 그대로 넘겨줘야 하는 데이터
     *    -> 처음에 넘겨준 데이터를 모두 읽어옴
     **/
    fun getIntentData(){
        isUpdate = intent.getBooleanExtra("isUpdate", true)
        update_id = intent.getLongExtra("Id", -1)
        todo = intent.getStringExtra("Todo")
        location = intent.getStringExtra("Location")
        rgb = intent.getIntExtra("Rgb", Color.parseColor("#cfc1c1"))
        date = intent.getStringExtra("date").toString()
        planner_id = intent.getLongExtra("plannerId", -1)
        selectedPlanner = intent.getParcelableExtra("PlannerEntity")!!
        time = intent.getIntExtra("Time", -1)
        last_select_palette = intent.getIntExtra("last_select_palette",0)
    }
}