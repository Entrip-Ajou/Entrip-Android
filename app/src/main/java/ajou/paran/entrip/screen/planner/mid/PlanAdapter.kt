package ajou.paran.entrip.screen.planner.mid

import ajou.paran.entrip.databinding.ItemLayoutPlanBinding
import ajou.paran.entrip.model.PlanEntity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class PlanAdapter : ListAdapter<PlanEntity,PlanAdapter.PlanViewHolder>(PlanDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = ItemLayoutPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlanViewHolder(private val binding : ItemLayoutPlanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(planEntity : PlanEntity){
            binding.plan = planEntity
            // todo : time(int) 를 String으로 변환하여 setText해야 한다.
            binding.executePendingBindings()
        }
    }
}

class PlanDiffCallback : DiffUtil.ItemCallback<PlanEntity>(){
    override fun areItemsTheSame(oldItem: PlanEntity, newItem: PlanEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PlanEntity, newItem: PlanEntity): Boolean {
        return oldItem == newItem
    }

}
