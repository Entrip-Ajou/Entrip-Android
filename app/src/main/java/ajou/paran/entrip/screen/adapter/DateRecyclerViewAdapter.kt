package ajou.paran.entrip.screen.adapter

import ajou.paran.entrip.R
import ajou.paran.entrip.model.PlannerDate
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

class DateRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dateItemList: List<PlannerDate>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        = DateItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout_date, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        dateItemList?.let { (holder as DateItemViewHolder).bind(it[position]) }
    }

    override fun getItemCount(): Int = dateItemList?.size ?: 0

    fun submitList(list: List<PlannerDate>?){
        dateItemList = list
        notifyDataSetChanged()
    }

    inner class DateItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(plannerDate: PlannerDate){
            itemView.findViewById<TextView>(R.id.itemLayout_tv_month).text = "${plannerDate.month}월"
            itemView.findViewById<TextView>(R.id.itemLayout_tv_day).text = "${plannerDate.day}"
            itemView.setOnClickListener {
                Log.d("DateRecyclerViewAdapter", "Click Case: ${plannerDate.month}월 ${plannerDate.day}일")
                // 해당 부분에서 플래너 날짜별 세부 내용과 연동 필요



            }
        }
    }
}