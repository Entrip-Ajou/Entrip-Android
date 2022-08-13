package ajou.paran.entrip.screen.home

import ajou.paran.entrip.databinding.ItemLayoutHomeRecommendationBinding
import ajou.paran.entrip.repository.network.dto.TripResponse
import ajou.paran.entrip.util.ui.RecommendDiffCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HomeRecommendAdapter
    : ListAdapter<TripResponse, RecyclerView.ViewHolder>(RecommendDiffCallback()) {
    companion object{
        const val TAG = "[HomeRecommendAdapter]"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = HomeRecommendViewHolder(
        ItemLayoutHomeRecommendationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    = (holder as HomeRecommendViewHolder).bind(getItem(position))

    fun setList(list: List<TripResponse>) {
        submitList(list)
    }

    inner class HomeRecommendViewHolder
    constructor(
        private val binding: ItemLayoutHomeRecommendationBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TripResponse) {
            Glide.with(binding.root)
                .load(item.photoUrl)
                .override(300,300)
                .centerCrop()
                .into(binding.itemRecommedImage)
        }

    }
}