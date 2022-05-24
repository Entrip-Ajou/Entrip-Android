package ajou.paran.entrip.screen.planner.main


import ajou.paran.entrip.R
import ajou.paran.entrip.databinding.ActivityMainBinding
import ajou.paran.entrip.model.InviteEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import ajou.paran.entrip.util.ApiState
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.invitation_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainAdapter.ItemClickListener, InviteAdapter.TextViewClickListner {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    companion object {
        private const val TAG = "[MainActivity]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        observeState()
        setContentView(view)
        setUpInviteFlag()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val mainAdapter = MainAdapter(this@MainActivity)
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

    private fun setUpInviteFlag() {
        lifecycle.coroutineScope.launch{
            viewModel.countInvite()
                .collect{
                    withContext(Dispatchers.Main){
                        if(it == 0) binding.icInviteFlag.visibility = View.GONE
                        else binding.icInviteFlag.visibility = View.VISIBLE
                    }
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
            val intent = Intent(this, PlannerActivity::class.java)
            intent.putExtra("PlannerEntity", data)
            startActivity(intent)
            finish()
        }
    }

    /**
     *    <<< 개발 과정에서 추가적인 Error 발생 시 이쪽에 추가하기 >>>
     *    0 -> NoInternetException
     *    -1 -> Exception
     *    500 -> Internal Server Error
     *
     */
    private fun handleError(code: Int) {
        when (code) {
            0 -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("네트워크를 확인해주세요")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, which -> })
                builder.show()
            }

            500 -> {
                Toast.makeText(this, "다른 사용자에 의해 삭제된 플래너입니다.", Toast.LENGTH_LONG).show()
            }

            -1 -> {
                Log.e(TAG, "최상위 Exception class에서 예외 발생 -> 코드 로직 오류")
            }

            else -> {
                Log.e(TAG, "${code} Error handleError()에 추가 및 trouble shooting하기")
            }
        }
    }

    override fun onDeletePlannerClickListener(plannerEntity: PlannerEntity) {
        viewModel.deletePlanner(plannerEntity.planner_id)
    }

    override fun onPlannerClickListener(plannerEntity: PlannerEntity) {
        viewModel.selectPlanner(plannerEntity.planner_id)
    }

    override fun onPlannerAddClickListener() {
        val user_id = sharedPreferences.getString("user_id", null).toString()
        viewModel.createPlanner(user_id)
    }

    fun click_notification(v: View) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.invitation_dialog, null)
        setUpInvitationRecyclerView(mDialogView)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()

        mDialogView.img_close.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun setUpInvitationRecyclerView(v : View){
        val inviteAdapter = InviteAdapter(this@MainActivity)
        v.rv_invite_log.adapter = inviteAdapter

        lifecycle.coroutineScope.launch {
            viewModel.selectAllInvite()
                .onStart { viewModel.setLoading() }
                .catch { e ->
                    viewModel.hideLoading()
                }
                .collect {
                    viewModel.hideLoading()
                    inviteAdapter.submitList(it.toList())

                    withContext(Dispatchers.Main){
                        if(inviteAdapter.itemCount == 0){
                            v.isInviteText.text = "도착한 초대장이 없어요"
                            v.rv_invite_log.visibility = View.GONE
                        }else{
                            v.isInviteText.text = "초대장이 도착했어요 !"
                            v.rv_invite_log.visibility = View.VISIBLE
                        }
                    }
                }
        }
    }

    override fun onAcceptInvitation(inviteEntity: InviteEntity) {
        
    }

    override fun onRejectInvitation(inviteEntity: InviteEntity) {

    }
}