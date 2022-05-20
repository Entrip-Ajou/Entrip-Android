package ajou.paran.entrip.screen.home

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityHomeBinding
import ajou.paran.entrip.screen.planner.main.MainActivity
import ajou.paran.entrip.screen.recommendation.RecommendationFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity: BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    companion object{
        const val TAG = "[HomeActivity]"
    }

    private val viewModel: HomeActivityViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        setUpBottomNavigationBar()
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
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    true
                }
                R.id.nav_recommendation -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.homeAct_nav_host_container, RecommendationFragment()).commit()
                    true
                }
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

    fun changeFrag(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeAct_nav_host_container, fragment).commit()
        when(fragment){
            is HomeFragment -> binding.homeActBottomNav.selectedItemId = R.id.nav_home
            is RecommendationFragment -> binding.homeActBottomNav.selectedItemId = R.id.nav_recommendation
            else -> R.id.nav_home
        }
    }

}