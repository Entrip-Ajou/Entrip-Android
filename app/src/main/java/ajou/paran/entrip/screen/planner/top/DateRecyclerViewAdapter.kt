package ajou.paran.entrip.screen.planner.top

import ajou.paran.entrip.R
import ajou.paran.entrip.screen.planner.mid.MidFragment
import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * @ClassName: DateRecyclerViewAdapter
 * @inheritance: RecyclerView.Adapter
 * @innerClass: DateItemViewHolder
 * **/

class DateRecyclerViewAdapter
constructor(
    private val midFragment: MidFragment,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedItemPos = 0
    private var lastItemSelectedPos = 0
    private var dateItemList: List<PlannerDate>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = DateItemViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_layout_date, parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(position == selectedItemPos) (holder as DateItemViewHolder).selected()
        else (holder as DateItemViewHolder).defaultSelected()
        dateItemList?.let { holder.bind(it[position]) }
    }

    override fun getItemCount(): Int = dateItemList?.size ?: 0

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<PlannerDate>?){
        dateItemList = list
        selectedItemPos = 0
        notifyDataSetChanged()
    }

    inner class DateItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(plannerDate: PlannerDate){
            val (year, month, day) = plannerDate.date.split("/")

            itemView.findViewById<TextView>(R.id.itemLayout_tv_month).text = "${month.toInt()}월"
            itemView.findViewById<TextView>(R.id.itemLayout_tv_day).text = day
            itemView.setOnClickListener {
                lastItemSelectedPos = selectedItemPos
                selectedItemPos = adapterPosition

                if(lastItemSelectedPos != selectedItemPos){
                    notifyItemChanged(lastItemSelectedPos)
                    notifyItemChanged(selectedItemPos)
                    midFragment.setAdapter(date = plannerDate.date)
                }
            }
        }

        fun defaultSelected() {
            itemView.findViewById<TextView>(R.id.itemLayout_tv_month).setTextColor(Color.parseColor("#7a7a7a"))
            itemView.findViewById<TextView>(R.id.itemLayout_tv_day).setTextColor(Color.parseColor("#7a7a7a"))
        }

        fun selected() {
            itemView.findViewById<TextView>(R.id.itemLayout_tv_month).setTextColor(Color.parseColor("#2d95eb"))
            itemView.findViewById<TextView>(R.id.itemLayout_tv_day).setTextColor(Color.parseColor("#2d95eb"))
        }

    }
}