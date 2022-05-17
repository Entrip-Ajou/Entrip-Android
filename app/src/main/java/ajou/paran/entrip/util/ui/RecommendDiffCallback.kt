package ajou.paran.entrip.util.ui

import ajou.paran.entrip.model.test.RecommendationItem
import androidx.recyclerview.widget.DiffUtil

class RecommendDiffCallback: DiffUtil.ItemCallback<RecommendationItem>() {
    override fun areItemsTheSame(oldItem: RecommendationItem, newItem: RecommendationItem): Boolean
            = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: RecommendationItem, newItem: RecommendationItem): Boolean
            = oldItem == newItem
}