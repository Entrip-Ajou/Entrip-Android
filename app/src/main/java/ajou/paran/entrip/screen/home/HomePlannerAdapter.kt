package ajou.paran.entrip.screen.home

import ajou.paran.entrip.databinding.ItemLayoutHomePlannerBinding
import ajou.paran.entrip.databinding.ItemLayoutHomePlannerHeaderBinding
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
        private const val HEADER_VIEW_TYPE = 0
        private const val ITEM_VIEW_TYPE = 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    =   if(viewType == HEADER_VIEW_TYPE)
            HeaderHomePlannerViewHolder(ItemLayoutHomePlannerHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener)
        else
            HomePlannerViewHolder(ItemLayoutHomePlannerBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HomePlannerViewHolder){
            holder.bind(getItem(position))
        }else if(holder is HeaderHomePlannerViewHolder){
            holder.bind()
        }
    }

    override fun getItemViewType(position : Int) : Int
    =   if(position == 0) {
            HEADER_VIEW_TYPE
        } else {
            ITEM_VIEW_TYPE
        }

    inner class HomePlannerViewHolder(private val binding: ItemLayoutHomePlannerBinding, listener: ItemClickListener)
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
            binding.homeTvPlannerDate.text = "${plannerEntity.start_date.split("/")[1]}/${plannerEntity.start_date.split("/")[2]}~${plannerEntity.end_date.split("/")[1]}/${plannerEntity.end_date.split("/")[2]}"
            binding.homeTvPlannerMonth.text = "${plannerEntity.start_date.split("/")[1].toInt().toString()}월"
            binding.homeImgPlannerDelete.setOnClickListener {
                listener.onDeletePlannerClickListener(plannerEntity)
            }
        }
    }

    inner class HeaderHomePlannerViewHolder(binding: ItemLayoutHomePlannerHeaderBinding, listener: ItemClickListener)
        : RecyclerView.ViewHolder(binding.root) {
        init{
            binding.btnPlanAdd.setOnClickListener {
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