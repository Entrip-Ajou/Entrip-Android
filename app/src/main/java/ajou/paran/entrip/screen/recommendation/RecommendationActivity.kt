package ajou.paran.entrip.screen.recommendation

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityRecommendationBinding
import ajou.paran.entrip.model.test.fakeRecommenItem
import ajou.paran.entrip.screen.planner.main.MainActivity
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendationActivity
:   BaseActivity<ActivityRecommendationBinding>(R.layout.activity_recommendation){
    companion object{
        const val TAG = "[RecommendationFrag]"
    }

    private val viewModel: RecommenActViewModel by viewModels()

    private lateinit var recommendItemAdapter: RecommendItemAdapter

    override fun init(savedInstanceState: Bundle?) {
        viewModel.getFakeTestItem()
        recommendItemAdapter = RecommendItemAdapter()

        binding.recommActRV.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = recommendItemAdapter
        }
        setUpBottomNavigationBar()
        subscribeObservers()
    }

    private fun subscribeObservers() = viewModel.recommendItemList.observe(
        this, Observer {
            recommendItemAdapter.apply {
                setList(fakeRecommenItem)
            }
        }
    )

    private fun setUpBottomNavigationBar(){
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.recomAct_bottom_nav)
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_planner -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    true
                }
                R.id.nav_recommendation -> {
//                    startActivity(Intent(applicationContext, RecommendationActivity::class.java))
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.nav_recommendation
    }
}