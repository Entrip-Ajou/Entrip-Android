package ajou.paran.entrip.screen.recommendation

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentRecommendationBinding
import ajou.paran.entrip.model.test.fakeRecommenItem
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendationFragment: BaseFragment<FragmentRecommendationBinding>(R.layout.fragment_recommendation) {
    companion object{
        const val TAG = "[RecommendationFrag]"
    }

    private val viewModel: RecommenFragViewModel by viewModels()

    private lateinit var recommendItemAdapter: RecommendItemAdapter

    override fun init() {
        viewModel.getFakeTestItem()
        recommendItemAdapter = RecommendItemAdapter()

        binding.recommFragRV.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = recommendItemAdapter
        }
        subscribeObservers()
    }

    private fun subscribeObservers() = viewModel.recommendItemList.observe(
        this, Observer {
            recommendItemAdapter.apply {
                setList(fakeRecommenItem)
            }
        }
    )
}