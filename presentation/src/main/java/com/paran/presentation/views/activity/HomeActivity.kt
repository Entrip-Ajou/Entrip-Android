package com.paran.presentation.views.activity

import ajou.paran.domain.model.BasePlanner
import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETActivity
import com.paran.presentation.common.route.HomeRoute
import com.paran.presentation.databinding.ActivityHomeBinding
import com.paran.presentation.utils.state.PlanState
import com.paran.presentation.utils.state.PlannerState
import com.paran.presentation.views.fragment.*
import com.paran.presentation.views.viewmodel.HomeActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@AndroidEntryPoint
class HomeActivity : BaseETActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private val viewModel: HomeActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        subObserver()
        setUpBottomNavigationBar()
//        setUpInviteFlag()
    }

    private fun subObserver() {
        viewModel.route.observe(this) { route ->
            when (route) {
                is HomeRoute.Planner -> {
                    visibleAppBar()
                    supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, PlannerFragment.newInstance()).commit()
                }
                is HomeRoute.PlannerDetail -> {
                    binding.homeBottomNav.selectedItemId = R.id.nav_planner
                    when (val planner = viewModel.detailPlannerState.value) {
                        is PlannerState.Store -> {
                            viewGoneAppBar()
                            supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, PlannerDetailFragment.newInstance(planner.data)).commit()
                        }
                        else -> {
                            viewModel.pushRoute(HomeRoute.Planner.tag)
                        }
                    }
                }
                is HomeRoute.PlanInput -> {
                    viewGoneAppBar()
                    when (val state = viewModel.detailPlanState.value) {
                        is PlanState.Store -> supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, PlanInputFragment.newInstance(
                            plannerId = state.plannerId,
                            selectedDate = state.selectedDate
                        )).commit()
                        else -> viewModel.pushRoute(HomeRoute.Planner.tag)
                    }
                }
                is HomeRoute.Notice -> {
                    viewGoneAppBar()
                    supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, NoticeFragment.newInstance()).commit()
                }
                is HomeRoute.Vote -> {
                    viewGoneAppBar()
                    supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, VoteFragment.newInstance()).commit()
                }
                is HomeRoute.PlannerUserAdd -> {
                    viewGoneAppBar()
                    supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, PlannerUserAddFragment.newInstance()).commit()
                }
                is HomeRoute.Recommendation -> {
                    visibleAppBar()
                    supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, RecommendationFragment.newInstance()).commit()
                }
                is HomeRoute.Community -> {
                    viewGoneAppBar()
                    supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, CommunityFragment.newInstance()).commit()
                }
                is HomeRoute.MyPage -> {
                    visibleAppBar()
                    supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, MyPageFragment.newInstance()).commit()
                }
                else -> {
                    visibleAppBar()
                    supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, HomeFragment.newInstance()).commit()
                }
            }
        }
    }

    @SuppressLint("CommitTransaction")
    private fun setUpBottomNavigationBar(){
        binding.homeBottomNav.run {
            setOnItemSelectedListener {
                when(it.itemId){
                    R.id.nav_home -> {
                        viewModel.pushRoute(HomeRoute.Home.tag)
                        true
                    }
                    R.id.nav_planner -> {
                        if (viewModel.route.value?.tag != HomeRoute.PlannerDetail.tag) {
                            viewModel.pushRoute(HomeRoute.Planner.tag)
                        }
                        true
                    }
                    R.id.nav_recommendation -> {
                        viewModel.pushRoute(HomeRoute.Recommendation.tag)
                        true
                    }
                    R.id.nav_board -> {
                        viewModel.pushRoute(HomeRoute.Community.tag)
                        true
                    }
                    R.id.nav_mypage -> {
                        viewModel.pushRoute(HomeRoute.MyPage.tag)
                        true
                    }
                    else -> false
                }
            }
            setOnItemReselectedListener {
                when(it.itemId){
                    else -> { }
                }
            }
        }
    }

    @Deprecated("Deprecated DebugHash Key")
    private fun getDebugHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            if (packageInfo == null) Log.e("KeyHash", "KeyHash:null") else {
                for (signature in packageInfo.signatures) {
                    val md: MessageDigest = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
                }
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun viewGoneAppBar() {
        binding.homeActTop.visibility = View.GONE
    }

    private fun visibleAppBar() {
        binding.homeActTop.visibility = View.VISIBLE
    }

    fun initPlannerData(planner: BasePlanner) {
        viewModel.initPlannerData(planner)
    }

    fun initPlanData(
        plannerId: Long,
        selectedDate: String
    ) {
        viewModel.initPlanData(plannerId, selectedDate)
    }

    fun cleanPlannerData() {
        viewModel.cleanPlannerData()
    }

    fun pushRoute(routeName: String) {
        viewModel.pushRoute(routeName)
    }

}