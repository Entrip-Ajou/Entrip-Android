package ajou.paran.entrip.screen.recommendation

import ajou.paran.entrip.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FlexBoxListAdapter
constructor(
    val list: List<String>
) : RecyclerView.Adapter<FlexBoxListAdapter.TagsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder
    = TagsViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_layout_tags, parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int)
    = holder.bind(list[position])

    inner class TagsViewHolder
    constructor(
        val itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(tag: String) {
            itemView.findViewById<TextView>(R.id.tv_tag).text = tag.trim()
        }
    }
}