package com.paran.presentation.views.activity

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.viewModels
import com.paran.presentation.R
import com.paran.presentation.common.base.BaseETActivity
import com.paran.presentation.databinding.ActivityHomeBinding
import com.paran.presentation.views.fragment.*
import com.paran.presentation.views.viewmodel.HomeActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@AndroidEntryPoint
class HomeActivity : BaseETActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private val viewModel: HomeActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        setUpBottomNavigationBar()
//        observeState()
//        setUpInviteFlag()
    }

    @SuppressLint("CommitTransaction")
    private fun setUpBottomNavigationBar(){
        supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, HomeFragment.newInstance()).commit()

        binding.homeBottomNav.run {
            setOnItemSelectedListener {
                when(it.itemId){
                    R.id.nav_home -> {
                        supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, HomeFragment.newInstance()).commit()
                        true
                    }
                    R.id.nav_planner -> {
                        supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, PlannerFragment.newInstance()).commit()
                        true
                    }
                    R.id.nav_recommendation -> {
                        supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, RecommendationFragment.newInstance()).commit()
                        true
                    }
                    R.id.nav_board -> {
                        supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, CommunityFragment.newInstance()).commit()
                        true
                    }
                    R.id.nav_mypage -> {
                        supportFragmentManager.beginTransaction().replace(R.id.homeAct_nav_host_container, MyPageFragment.newInstance()).commit()
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
            selectedItemId = intent.getIntExtra("last_pos", R.id.nav_home)
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

}