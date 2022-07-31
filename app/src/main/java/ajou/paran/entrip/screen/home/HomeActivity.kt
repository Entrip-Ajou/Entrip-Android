package ajou.paran.entrip.screen.home

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityHomeBinding
import ajou.paran.entrip.model.InviteEntity
import ajou.paran.entrip.repository.network.dto.NotificationData
import ajou.paran.entrip.repository.network.dto.PushNotification
import ajou.paran.entrip.screen.planner.main.InviteAdapter
import ajou.paran.entrip.screen.planner.main.MainFragment
import ajou.paran.entrip.screen.recommendation.RecommendationFragment
import ajou.paran.entrip.util.ApiState
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.invitation_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity: BaseActivity<ActivityHomeBinding>(R.layout.activity_home), InviteAdapter.TextViewClickListner {
    companion object{
        const val TAG = "[HomeActivity]"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: HomeActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        setUpBottomNavigationBar()
        if(intent.getBooleanExtra("isFromPlanner", false)){
            supportFragmentManager.beginTransaction()
                .replace(R.id.homeAct_nav_host_container, MainFragment()).commit()
        }
        observeState()
        setUpInviteFlag()
    }

    private fun setUpBottomNavigationBar(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeAct_nav_host_container, HomeFragment()).commit()

        binding.homeActBottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.homeAct_nav_host_container, HomeFragment()).commit()
                    true
                }
                R.id.nav_planner -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.homeAct_nav_host_container, MainFragment()).commit()
                    true
                }
                R.id.nav_recommendation -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.homeAct_nav_host_container, RecommendationFragment()).commit()
                    true
                }
//                R.id.nav_board -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.homeAct_nav_host_container, CommunityFragment()).commit()
//                    true
//                }
                else -> false
            }
        }

        binding.homeActBottomNav.setOnItemReselectedListener {
            when(it.itemId){
                else -> { }
            }
        }

        binding.homeActBottomNav.selectedItemId = intent.getIntExtra("last_pos", R.id.nav_home)
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
            is ApiState.Failure -> handleError(state.code)
            else -> { }
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbLoadingBar.visibility = View.VISIBLE
        } else {
            binding.pbLoadingBar.visibility = View.GONE
        }
    }

    private fun handleError(code: Int) {
        when (code) {
            0 -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("네트워크를 확인해주세요")
                    .setPositiveButton("확인") { dialog, which -> dialog.dismiss() }
                builder.show()
            }

            500 -> {
                Toast.makeText(this, "다른 사용자에 의해 삭제된 플래너입니다.", Toast.LENGTH_LONG).show()
            }

            -1 -> {
                Log.e(TAG, "최상위 Exception class에서 예외 발생 -> 코드 로직 오류")
            }

            else -> {
                Log.e(TAG, "$code Error handleError()에 추가 및 trouble shooting하기")
            }
        }
    }


    fun changeFrag(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeAct_nav_host_container, fragment).commit()
        when(fragment){
            is HomeFragment -> { binding.homeActBottomNav.selectedItemId = R.id.nav_home }
            is RecommendationFragment -> { binding.homeActBottomNav.selectedItemId = R.id.nav_recommendation }
            else -> { R.id.nav_home }
        }
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

    private fun setUpInviteFlag()
    = lifecycle.coroutineScope.launch {
        viewModel.countInvite()
            .collect {
                withContext(Dispatchers.Main) {
                    when(it) {
                        0 -> { binding.icInviteFlag.visibility = View.GONE }
                        else -> {  binding.icInviteFlag.visibility = View.VISIBLE }
                    }
                }
            }
    }


    private fun setUpInvitationRecyclerView(v: View) {
        val inviteAdapter = InviteAdapter(this@HomeActivity)
        v.rv_invite_log.adapter = inviteAdapter

        lifecycle.coroutineScope.launch {
            viewModel.selectAllInvite()
                .onStart { viewModel.setLoading() }
                .catch { viewModel.hideLoading() }
                .collect {
                    viewModel.hideLoading()
                    inviteAdapter.submitList(it.toList())

                    withContext(Dispatchers.Main) {
                        when(inviteAdapter.itemCount) {
                            0 -> {
                                v.isInviteText.text = "도착한 초대장이 없어요"
                                v.rv_invite_log.visibility = View.GONE
                            }
                            else -> {
                                v.isInviteText.text = "초대장이 도착했어요 !"
                                v.rv_invite_log.visibility = View.VISIBLE
                            }
                        }
                    }
                }
        }
    }


    override fun onAcceptInvitation(inviteEntity: InviteEntity) {
        val notificationData = PushNotification(
            data = NotificationData(
                title = "Entrip",
                message = "${sharedPreferences.getString("nickname", null).toString()
                }님이 플래너 초대를 수락하셨습니다",
                owner_id = sharedPreferences.getString("user_id", null).toString(),
                owner = sharedPreferences.getString("nickname", null).toString(),
                owner_token = sharedPreferences.getString("token", null).toString(),
                photo_url = sharedPreferences.getString("photo_url", null).toString(),
                planner_id = inviteEntity.planner_id,
                planner_title = inviteEntity.planner_title,
                isInvite = false
            ),
            to = inviteEntity.token
        )

        sharedPreferences.getString("user_id", null)?.let { user_id ->
            viewModel.acceptInvitation(inviteEntity, user_id, notificationData)
        }
    }

    override fun onRejectInvitation(inviteEntity: InviteEntity) {
        val notificationData = PushNotification(
            data = NotificationData(
                title = "Entrip",
                message = "${sharedPreferences.getString("nickname", null).toString()
                }님이 플래너 초대를 거절하셨습니다",
                owner_id = sharedPreferences.getString("user_id", null).toString(),
                owner = sharedPreferences.getString("nickname", null).toString(),
                owner_token = sharedPreferences.getString("token", null).toString(),
                photo_url = sharedPreferences.getString("photo_url", null).toString(),
                planner_id = inviteEntity.planner_id,
                planner_title = inviteEntity.planner_title,
                isInvite = false
            ),
            to = inviteEntity.token
        )
        viewModel.rejectInvitation(inviteEntity, notificationData)
    }

}