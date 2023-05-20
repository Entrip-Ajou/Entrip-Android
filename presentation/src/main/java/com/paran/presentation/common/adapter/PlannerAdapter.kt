package com.paran.presentation.common.adapter

import ajou.paran.domain.model.BasePlanner
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paran.presentation.databinding.ItemPlannerBinding
import com.paran.presentation.databinding.ItemPlannerFooterBinding
import com.paran.presentation.utils.callbacks.BasePlannerDiffCallback

class PlannerAdapter
constructor(
    private val onClickPlanner: (planner: BasePlanner) -> Unit,
    private val onClickAdd: () -> Unit,
) : ListAdapter<BasePlanner, RecyclerView.ViewHolder>(BasePlannerDiffCallback()) {
    companion object{
        private const val FOOTER_VIEW_TYPE = 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        FOOTER_VIEW_TYPE -> FooterHomePlannerViewHolder(
            ItemPlannerFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick = onClickAdd
        )
        else -> HomePlannerViewHolder(
            ItemPlannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick = onClickPlanner
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when(holder){
            is HomePlannerViewHolder -> holder.bind(getItem(position))
            is FooterHomePlannerViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + 1

    override fun getItemViewType(position: Int): Int = when(position){
        itemCount - 1 -> FOOTER_VIEW_TYPE
        else -> super.getItemViewType(position)
    }

    inner class HomePlannerViewHolder(
        private val binding: ItemPlannerBinding,
        private val onClick: (planner: BasePlanner) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                clPlanner.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION){
                        onClick(getItem(adapterPosition))
                    }
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(planner: BasePlanner){
            binding.tvPlannerName.text = planner.title
            binding.tvPlannerDate.text = "${planner.startDate.split("/")[1]}/${planner.startDate.split("/")[2]}~${planner.endDate.split("/")[1]}/${planner.endDate.split("/")[2]}"
            binding.tvPlannerMonth.text = "${planner.startDate.split("/")[1].toInt()}월"
        }

    }

    inner class FooterHomePlannerViewHolder(
        binding: ItemPlannerFooterBinding,
        private val onClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnPlanAdd.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION){
                    onClick()
                }
            }
        }

        fun bind() {

        }

    }

}