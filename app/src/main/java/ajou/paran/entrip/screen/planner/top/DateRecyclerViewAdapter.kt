package ajou.paran.entrip.screen.planner.top

import ajou.paran.entrip.R
import ajou.paran.entrip.model.PlannerDate
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
    private val midFragment: MidFragment
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

//    private var checkedItemView: View? = null
    private var selectedItemPos = 0
    private var lastItemSelectedPos = 0
    private var dateItemList: List<PlannerDate>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = DateItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout_date, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == selectedItemPos)
            (holder as DateItemViewHolder).selected()
        else
            (holder as DateItemViewHolder).defaultSelected()
        dateItemList?.let { (holder as DateItemViewHolder).bind(it[position]) }
    }

    override fun getItemCount(): Int = dateItemList?.size ?: 0

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<PlannerDate>?){
        dateItemList = list
        notifyDataSetChanged()
    }

    inner class DateItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(plannerDate: PlannerDate){
            val (year, month, day) = plannerDate.date.split("/")
//            if (plannerDate.date == midFragment.getDate()){
//                itemView.findViewById<TextView>(R.id.itemLayout_tv_month).setTextColor(Color.CYAN)
//                itemView.findViewById<TextView>(R.id.itemLayout_tv_day).setTextColor(Color.CYAN)
//                checkedItemView = itemView
//            }
            itemView.findViewById<TextView>(R.id.itemLayout_tv_month).text = "${month.toInt()}월"
            itemView.findViewById<TextView>(R.id.itemLayout_tv_day).text = day
            itemView.setOnClickListener {
                Log.d("DateRecyclerViewAdapter", "Click Case: $month 월 $day 일")
                // 해당 부분에서 플래너 날짜별 세부 내용과 연동 필요
                selectedItemPos = adapterPosition
                lastItemSelectedPos = if (lastItemSelectedPos == -1)
                    selectedItemPos
                else{
                    notifyItemChanged(lastItemSelectedPos)
                    selectedItemPos
                }
                notifyItemChanged(selectedItemPos)
//                checkSelected(itemView)
//                itemView.findViewById<TextView>(R.id.itemLayout_tv_month).setTextColor(Color.CYAN)
//                itemView.findViewById<TextView>(R.id.itemLayout_tv_day).setTextColor(Color.CYAN)
//                checkedItemView = itemView
                midFragment.setAdapter(date = plannerDate.date)
            }
        }

        fun defaultSelected() {
            itemView.findViewById<TextView>(R.id.itemLayout_tv_month).setTextColor(Color.BLACK)
            itemView.findViewById<TextView>(R.id.itemLayout_tv_day).setTextColor(Color.BLACK)
        }

        fun selected(){
            itemView.findViewById<TextView>(R.id.itemLayout_tv_month).setTextColor(Color.CYAN)
            itemView.findViewById<TextView>(R.id.itemLayout_tv_day).setTextColor(Color.CYAN)
        }

//        private fun checkSelected(itemView: View) = checkedItemView?.let {
//            if(checkedItemView!!.id == itemView.id){
//                checkedItemView!!.findViewById<TextView>(R.id.itemLayout_tv_month).setTextColor(Color.BLACK)
//                checkedItemView!!.findViewById<TextView>(R.id.itemLayout_tv_day).setTextColor(Color.BLACK)
//            }
//        }

    }
}