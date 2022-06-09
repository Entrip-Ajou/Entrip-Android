package ajou.paran.entrip.screen.recommendation

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseFragment
import ajou.paran.entrip.databinding.FragmentRecommendationBinding
import ajou.paran.entrip.model.test.nullRecommenItem
import android.content.SharedPreferences
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class RecommendationFragment: BaseFragment<FragmentRecommendationBinding>(R.layout.fragment_recommendation) {
    companion object{
        const val TAG = "[RecommendationFrag]"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: RecommenFragViewModel by viewModels()

    private lateinit var recommendItemAdapter: RecommendItemAdapter

    override fun init() {
        recommendItemAdapter = RecommendItemAdapter()
        binding.recommFragRV.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = recommendItemAdapter
        }
        viewModel.findByUserId(sharedPreferences.getString("user_id", null).toString())
        subscribeObservers()
    }

    private fun subscribeObservers() = viewModel.recommendItemList.observe(
        this, Observer {
            recommendItemAdapter.apply {
                setList(it)
            }
        }
    )
}