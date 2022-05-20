package ajou.paran.entrip.screen.home

import ajou.paran.entrip.R
import ajou.paran.entrip.model.test.RecommendationItem
import ajou.paran.entrip.screen.recommendation.FlexBoxListAdapter
import ajou.paran.entrip.util.ui.RecommendDiffCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class HomeRecommendAdapter
    : ListAdapter<RecommendationItem, RecyclerView.ViewHolder>(RecommendDiffCallback()) {
    companion object{
        const val TAG = "[HomeRecommendAdapter]"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        = HomeRecommendViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout_recommedation, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
        = (holder as HomeRecommendViewHolder).bind(getItem(position))

    fun setList(list: List<RecommendationItem>) {
        submitList(list)
    }

    inner class HomeRecommendViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: RecommendationItem) {
            itemView.findViewById<TextView>(R.id.recomItem_name).text = item.name
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .into(itemView.findViewById(R.id.recomItem_image))
            itemView.findViewById<RecyclerView>(R.id.recomItem_rv).apply {
                val flexBoxLayoutManager = FlexboxLayoutManager(itemView.context)
                flexBoxLayoutManager.flexDirection = FlexDirection.ROW
                flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager = flexBoxLayoutManager
                adapter = FlexBoxListAdapter(item.tags)
            }
        }
    }
}