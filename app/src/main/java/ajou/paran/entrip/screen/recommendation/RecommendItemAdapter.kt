package ajou.paran.entrip.screen.recommendation

import ajou.paran.entrip.R
import ajou.paran.entrip.model.test.RecommendationItem
import ajou.paran.entrip.screen.trip.TripTestActivity
import ajou.paran.entrip.util.ui.RecommendDiffCallback
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class RecommendItemAdapter
    : ListAdapter<RecommendationItem, RecyclerView.ViewHolder>(RecommendDiffCallback()) {
    companion object{
        private const val TYPE_HEADER = 0;
        private const val TYPE_ITEM = 1;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        =   if (viewType != TYPE_HEADER)
                RecommendItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout_recommedation, parent, false))
            else
                RecommendHeaderItemViewHolder(parent.context, LayoutInflater.from(parent.context).inflate(R.layout.item_layout_recommend_header, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int){
        if (holder is RecommendItemViewHolder)
            holder.bind(getItem(position))
        else if (holder is RecommendHeaderItemViewHolder)
            holder.bind()
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    fun setList(list: List<RecommendationItem>) {
        submitList(list)
    }

    inner class RecommendItemViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: RecommendationItem){
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

    inner class RecommendHeaderItemViewHolder constructor(private val context : Context, itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(){
            itemView.findViewById<Button>(R.id.recomAct_testBtn).setOnClickListener {
                Log.d("[RecommendHeaderItem]", "설문조사 하러 가기 눌림")
                context.startActivity(Intent(context, TripTestActivity::class.java))
            }
        }
    }

}