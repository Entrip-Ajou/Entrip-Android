package ajou.paran.entrip.screen.recommendation

import ajou.paran.entrip.databinding.ActivityRecommendBinding
import ajou.paran.entrip.screen.recommendation.model.AreaRecord
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class RecommendActivity : AppCompatActivity(), AreaAdapter.OnRatingChangedListener {

    private lateinit var binding: ActivityRecommendBinding
    private var areaList = ArrayList<List<AreaRecord>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        val view = binding.root
        init()
        setContentView(view)
    }

    private fun init() {
        setupRecycleView()
        setupTab()
    }

    private fun setupTab() {
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.rvArea.adapter?.let{
                        it -> (it as AreaAdapter).update(areaList[tab!!.position], tab!!.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    private fun setupRecycleView() {
        val areaJsonString = assets.open("area.json").bufferedReader().use { it.readText() }
        val json = JSONObject(areaJsonString)
        val area = json.getJSONArray("data")

        for(i in 0 until area.length()){
            val _area = area.getJSONObject(i)
            val city = _area.getString("city")
            val districtsArray = _area.getJSONArray("districts")
            val districts = mutableListOf<AreaRecord>()
            for (j in 0 until districtsArray.length()) {
                districts.add(AreaRecord(districtsArray.getString(j), 0))
            }

            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(city))
            areaList.add(districts)
            Log.d("JSON", "City: $city")
            Log.d("JSON", "Districts : $districts")
        }

        binding.rvArea.apply {
            adapter = AreaAdapter(areaList[0].toMutableList(),this@RecommendActivity, 0)
        }
    }

    override fun onRatingChanged(position: Int, rating: Int, index : Int) {
        Log.e("TEST", "position = $position")
        Log.e("TEST", "rating = $rating")
        Log.e("TEST", "index = $index")

    }
}