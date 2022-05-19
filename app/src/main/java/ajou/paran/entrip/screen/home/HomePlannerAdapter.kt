package ajou.paran.entrip.screen.home

import ajou.paran.entrip.databinding.ItemHomePlannerBinding
import ajou.paran.entrip.databinding.ItemHomePlannerFooterBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.screen.planner.main.PlannerDiffCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class HomePlannerAdapter
constructor
(
    val listener : ItemClickListener
) : ListAdapter<PlannerEntity, RecyclerView.ViewHolder>(PlannerDiffCallback()) {
    companion object{
        private const val FOOTER_VIEW_TYPE = 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == FOOTER_VIEW_TYPE){
            val binding = ItemHomePlannerFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return FooterHomePlannerViewHolder(binding, listener)
        }
        val binding =
            ItemHomePlannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomePlannerViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HomePlannerViewHolder){
            holder.bind(getItem(position))
        }else if(holder is FooterHomePlannerViewHolder){
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

    inner class HomePlannerViewHolder(private val binding: ItemHomePlannerBinding, listener: ItemClickListener)
        : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                homePlannerClick.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        listener.onPlannerClickListener(getItem(position))
                    }
                }
            }
        }

        fun bind(plannerEntity: PlannerEntity){
            binding.homeTvPlannerName.text = plannerEntity.title
            binding.homeImgPlannerDelete.setOnClickListener {
                listener.onDeletePlannerClickListener(plannerEntity)
            }
        }
    }

    inner class FooterHomePlannerViewHolder(binding: ItemHomePlannerFooterBinding, listener: ItemClickListener)
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