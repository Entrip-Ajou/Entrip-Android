package ajou.paran.entrip.screen.planner.main

import ajou.paran.entrip.databinding.ItemPlannerBinding
import ajou.paran.entrip.databinding.ItemPlannerFooterBinding
import ajou.paran.entrip.model.PlannerEntity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

private const val FOOTER_VIEW_TYPE = 1

class MainAdapter(val listener : ItemClickListener) : ListAdapter<PlannerEntity, RecyclerView.ViewHolder>(PlannerDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == FOOTER_VIEW_TYPE){
            val binding = ItemPlannerFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return FooterPlannerViewHolder(binding, listener)
        }
        val binding =
            ItemPlannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlannerViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is PlannerViewHolder){
            holder.bind(getItem(position))
        }else if(holder is FooterPlannerViewHolder){
            holder.bind()
        }
    }

    override fun getItemCount(): Int{
        return super.getItemCount()+1
    }

    override fun getItemViewType(position : Int) : Int{
        if(position == itemCount-1){
            return FOOTER_VIEW_TYPE
        }
        return super.getItemViewType(position)
    }


    inner class PlannerViewHolder(private val binding: ItemPlannerBinding, listener: ItemClickListener)
        : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                plannerClick.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        listener.onPlannerClickListener(getItem(position))
                    }
                }
            }
        }

        fun bind(plannerEntity: PlannerEntity){
            binding.tvPlannerName.text = plannerEntity.title
            binding.imgPlannerDelete.setOnClickListener {
                listener.onDeletePlannerClickListener(plannerEntity)
            }
        }
    }

    inner class FooterPlannerViewHolder(binding: ItemPlannerFooterBinding, listener: ItemClickListener)
        : RecyclerView.ViewHolder(binding.root) {
        init{
            binding.btnPlannerAdd.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    listener.onPlannerAddClickListener()
                }
            }
        }
        fun bind(){ }
    }

    interface ItemClickListener{
        fun onDeletePlannerClickListener(plannerEntity : PlannerEntity)
        fun onPlannerClickListener(plannerEntity: PlannerEntity)
        fun onPlannerAddClickListener()
    }
}

class PlannerDiffCallback : DiffUtil.ItemCallback<PlannerEntity>() {
    override fun areItemsTheSame(oldItem: PlannerEntity, newItem: PlannerEntity): Boolean {
        return oldItem.planner_id == newItem.planner_id
    }

    override fun areContentsTheSame(oldItem: PlannerEntity, newItem: PlannerEntity): Boolean {
        return oldItem == newItem
    }

}


