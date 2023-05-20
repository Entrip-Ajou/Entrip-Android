package ajou.paran.entrip.screen.recommendation


import ajou.paran.entrip.R
import ajou.paran.entrip.databinding.ItemLayoutRecommendationBinding
import ajou.paran.entrip.repository.network.dto.TripResponse
import ajou.paran.entrip.util.ui.RecommendDiffCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecommendAdapter
    : ListAdapter<TripResponse, RecyclerView.ViewHolder>(RecommendDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemLayoutRecommendationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecommendViewHolder(binding)
    }

    override fun getItemCount(): Int = super.getItemCount()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecommendViewHolder).bind(getItem(position))
    }

    inner class RecommendViewHolder(private val itemBinding : ItemLayoutRecommendationBinding)
        :RecyclerView.ViewHolder(itemBinding.root) {
            fun bind(t : TripResponse) {
                itemView.findViewById<TextView>(R.id.recomItem_name).text = t.name

                Glide.with(itemView)
                    .load(t.photoUrl)
                    .override(300, 300)
                    .centerCrop()
                    .into(itemView.findViewById(R.id.recomItem_image))
            }
    }
}