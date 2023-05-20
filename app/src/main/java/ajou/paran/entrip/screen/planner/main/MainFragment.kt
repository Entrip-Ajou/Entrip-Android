package ajou.paran.entrip.screen.planner.main

import android.view.View
import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentMainBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.screen.home.HomePlannerAdapter
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.ui.RecyclerViewDecoration
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main),
    MainPlannerAdapter.ItemClickListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: MainActivityViewModel by viewModels()

    companion object {
        const val TAG = "[MainFragment]"
    }

    override fun init() {
        observeState()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val mainAdapter = MainPlannerAdapter(this)
        binding.rvPlannerList.adapter = mainAdapter

        lifecycle.coroutineScope.launch {
            viewModel.selectAllPlanner()
                .onStart { viewModel.setLoading() }
                .catch { e ->
                    viewModel.hideLoading()
                }
                .collect {
                    viewModel.hideLoading()
                    mainAdapter.submitList(it.toList())
                }
        }
    }

    private fun observeState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: ApiState) {
        when (state) {
            is ApiState.Init -> Unit
            is ApiState.IsLoading -> handleLoading(state.isLoading)
            is ApiState.Success -> handleSuccess(state.data)
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

    private fun handleSuccess(data: Any) {
        if (data is PlannerEntity) {
            val intent = Intent(activity, PlannerActivity::class.java)
            intent.putExtra("PlannerEntity", data)
            startActivity(intent)
        }
    }

    private fun handleError(code: Int) {
        when (code) {
            0 -> {
                val builder = AlertDialog.Builder(requireActivity())
                builder.setMessage("네트워크를 확인해주세요")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, which -> })
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

    override fun onPlannerClickListener(plannerEntity: PlannerEntity) {
        val intent = Intent(activity, PlannerActivity::class.java)
        intent.putExtra("PlannerEntity", plannerEntity)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onPlannerAddClickListener() {
        val user_id = sharedPreferences.getString("user_id", null).toString()
        viewModel.createPlanner(user_id)
    }
}