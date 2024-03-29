package ajou.paran.entrip.screen.planner.mid

import ajou.paran.entrip.R
import ajou.paran.entrip.databinding.ItemLayoutPlanBinding
import ajou.paran.entrip.databinding.ItemLayoutPlanFooterBinding
import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.screen.planner.mid.input.InputActivity
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


private const val FOOTER_VIEW_TYPE = 1

class PlanAdapter(val listener: RowClickListener) : ListAdapter<PlanEntity, RecyclerView.ViewHolder>(PlanDiffCallback()) {

    lateinit var date: String
    var plannerId: Long = -1

    lateinit var selectedPlanner : PlannerEntity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == FOOTER_VIEW_TYPE) {
            val binding = ItemLayoutPlanFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return FooterViewHolder(binding).apply {
                binding.btnPlanAdd.setOnClickListener {
                    val intent = Intent(binding.btnPlanAdd.context, InputActivity::class.java)
                    intent.apply{
                        if (currentList.size != 0){
                            this.putExtra("isUpdate", false)
                            // getItem(adapterPosition) -> 현재 버튼이 가장 마지막 position이므로 현재 포지션에서 1을뺀 planEntity를 가져옴
                            // 오류가 발생하면 position 크기를 더 줄이면 된다!
                            this.putExtra("date", getItem(adapterPosition-1).date)
                            this.putExtra("plannerId", getItem(adapterPosition-1).planner_idFK)
                            this.putExtra("PlannerEntity", selectedPlanner)
                        } else {
                            this.putExtra("isUpdate", false)
                            this.putExtra("date", date)
                            this.putExtra("plannerId", plannerId)
                            this.putExtra("PlannerEntity", selectedPlanner)
                        }
                    }
                    ContextCompat.startActivity(binding.btnPlanAdd.context, intent, null)
                }
            }
        }
        val binding =
            ItemLayoutPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding, listener)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PlanViewHolder) {
            holder.bind(getItem(position))
        } else if (holder is FooterViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()+1       // PlanEntity 개수 + 1(Footer)
    }


    override fun getItemViewType(position: Int): Int {
        if (position == itemCount-1) {
            return FOOTER_VIEW_TYPE
        }
        return super.getItemViewType(position)
    }

    inner class FooterViewHolder(private val binding: ItemLayoutPlanFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }

    inner class PlanViewHolder(private val binding: ItemLayoutPlanBinding, val listener: RowClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        init{
            binding.apply {
                itemClick.setOnClickListener{
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClickListener(getItem(position))
                    }
                }
            }
        }

        fun bind(planEntity: PlanEntity) {
            val timeString = planEntity.time.toString()
            var hour = ""
            var minute = ""

            when (timeString.length) {
                1 -> {
                    hour = "00"
                    minute = "0" + timeString
                }
                2 -> {
                    hour = "00"
                    minute = timeString
                }
                3 -> {
                    hour = "0"+timeString.substring(0, 1)
                    minute = timeString.substring(timeString.length - 2, timeString.length)
                }
                4 -> {
                    hour = timeString.substring(0, 2)
                    minute = timeString.substring(timeString.length - 2, timeString.length)
                }
            }
            binding.tvItemTime.text = "$hour:$minute"

            if (planEntity.location.isNullOrEmpty()) binding.tvItemLocation.text = ""
            else binding.tvItemLocation.text = planEntity.location

            binding.tvItemTodo.text = planEntity.todo

            if(planEntity.isExistComments){
                binding.imgItemComment.setImageResource(R.drawable.comment_event)
            } else{
                binding.imgItemComment.setImageResource(R.drawable.comment_default)
            }

            binding.imgItemDelete.setOnClickListener{
                listener.onDeletePlanClickListener(planEntity)
            }

            binding.imgItemComment.setOnClickListener {
                listener.onCommentItemClickListener(planEntity)
            }

            binding.executePendingBindings()
        }
    }

    interface RowClickListener{
        fun onDeletePlanClickListener(planEntity: PlanEntity)
        fun onItemClickListener(planEntity: PlanEntity)
        fun onCommentItemClickListener(planEntity : PlanEntity)
    }
}

class PlanDiffCallback : DiffUtil.ItemCallback<PlanEntity>() {
    override fun areItemsTheSame(oldItem: PlanEntity, newItem: PlanEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PlanEntity, newItem: PlanEntity): Boolean {
        return oldItem == newItem
    }
}


