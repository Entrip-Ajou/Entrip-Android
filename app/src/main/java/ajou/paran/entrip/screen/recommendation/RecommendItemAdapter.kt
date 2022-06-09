package ajou.paran.entrip.screen.recommendation

import ajou.paran.entrip.R
import ajou.paran.entrip.repository.network.dto.TripResponse
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
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class RecommendItemAdapter
    : ListAdapter<TripResponse, RecyclerView.ViewHolder>(RecommendDiffCallback()) {
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
            holder.bind(getItem(position-1))
        else if (holder is RecommendHeaderItemViewHolder)
            holder.bind()
    }

    override fun getItemCount(): Int = super.getItemCount()+1

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    fun setList(list: List<TripResponse>) {
        submitList(list)
    }

    inner class RecommendItemViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: TripResponse){
            itemView.findViewById<TextView>(R.id.recomItem_name).text = item.name
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .override(300,300)
                .centerCrop()
                .into(itemView.findViewById(R.id.recomItem_image))
            itemView.findViewById<RecyclerView>(R.id.recomItem_rv).apply {
                val flexBoxLayoutManager = FlexboxLayoutManager(itemView.context)
                flexBoxLayoutManager.flexDirection = FlexDirection.ROW
                flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager = flexBoxLayoutManager
                val list = ArrayList<String>()
                item.tags.forEach { tag ->
                    when(tag){
                        "S" -> list.add("계획형")
                        "I" -> list.add("즉흥형")
                        "H" -> list.add("힐링형")
                        "A" -> list.add("액티비티형")
                        "P" -> list.add("인스타형")
                        "L" -> list.add("로컬형")
                        "F" -> list.add("파이어족")
                        "R" -> list.add("합리적")
                    }
                }
                adapter = FlexBoxListAdapter(list)
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