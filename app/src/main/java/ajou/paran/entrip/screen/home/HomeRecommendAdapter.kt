package ajou.paran.entrip.screen.home

import ajou.paran.entrip.databinding.ItemLayoutHomeRecommendationBinding
import ajou.paran.entrip.model.test.RecommendationItem
import ajou.paran.entrip.util.ui.RecommendDiffCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HomeRecommendAdapter
    : ListAdapter<RecommendationItem, RecyclerView.ViewHolder>(RecommendDiffCallback()) {
    companion object{
        const val TAG = "[HomeRecommendAdapter]"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        = HomeRecommendViewHolder(ItemLayoutHomeRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
        = (holder as HomeRecommendViewHolder).bind(getItem(position))

    fun setList(list: List<RecommendationItem>) {
        submitList(list)
    }

    inner class HomeRecommendViewHolder constructor(private val binding: ItemLayoutHomeRecommendationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecommendationItem) {
            Glide.with(binding.root)
                .load(item.photoUrl)
                .override(300,300)
                .centerCrop()
                .into(binding.itemRecommedImage)
        }
    }
}