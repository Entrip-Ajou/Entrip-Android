package ajou.paran.entrip.util.ui

import ajou.paran.entrip.repository.network.dto.TripResponse
import androidx.recyclerview.widget.DiffUtil

class RecommendDiffCallback: DiffUtil.ItemCallback<TripResponse>() {
    override fun areItemsTheSame(oldItem: TripResponse, newItem: TripResponse): Boolean
            = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: TripResponse, newItem: TripResponse): Boolean
            = oldItem == newItem
}