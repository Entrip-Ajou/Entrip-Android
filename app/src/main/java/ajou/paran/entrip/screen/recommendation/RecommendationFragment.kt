package ajou.paran.entrip.screen.recommendation

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentRecommendBinding
import ajou.paran.entrip.model.test.nullRecommenItem
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendationFragment: BaseFragment<FragmentRecommendBinding>(R.layout.fragment_recommend) {
    companion object{
        const val TAG = "[RecommendationFrag]"
    }

    private lateinit var recommendItemAdapter: RecommendAdapter

    override fun init() {
        binding.btnRecommendService.setOnClickListener {
            val intent = Intent(context, RecommendActivity::class.java)
            startActivity(intent)
        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView(){
        recommendItemAdapter = RecommendAdapter()

        binding.rvRecommend.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recommendItemAdapter
        }
        recommendItemAdapter.submitList(nullRecommenItem)
    }
}