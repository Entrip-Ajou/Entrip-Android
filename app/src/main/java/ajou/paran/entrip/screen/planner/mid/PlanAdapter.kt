package ajou.paran.entrip.screen.planner.mid

import ajou.paran.entrip.databinding.ItemLayoutPlanBinding
import ajou.paran.entrip.databinding.ItemLayoutPlanFooterBinding
import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.screen.planner.mid.input.InputActivity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


private const val FOOTER_VIEW_TYPE = 1

class PlanAdapter : ListAdapter<PlanEntity, RecyclerView.ViewHolder>(PlanDiffCallback()) {

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
                    ContextCompat.startActivity(binding.btnPlanAdd.context, intent, null)
                }
            }
        }
        val binding =
            ItemLayoutPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding)

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

    class FooterViewHolder(private val binding: ItemLayoutPlanFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }

    class PlanViewHolder(private val binding: ItemLayoutPlanBinding) :
        RecyclerView.ViewHolder(binding.root) {

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

            binding.itemLayout.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    planEntity.rgb
                )
            )
            binding.executePendingBindings()
        }
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


