package com.paran.presentation.common.adapter

import ajou.paran.domain.model.BasePlanner
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.paran.presentation.databinding.ItemPlannerHomeBinding
import com.paran.presentation.utils.callbacks.BasePlannerDiffCallback

class HomePlannerAdapter
constructor(
    private val onPlannerClickListener: (planner: BasePlanner) -> Unit,
) : ListAdapter<BasePlanner, RecyclerView.ViewHolder>(BasePlannerDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = HomePlannerViewHolder(
        ItemPlannerHomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        onPlannerClickListener = onPlannerClickListener
    )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        (holder as HomePlannerViewHolder).bind(getItem(position))
    }

    inner class HomePlannerViewHolder(
        private val binding: ItemPlannerHomeBinding,
        onPlannerClickListener: (planner: BasePlanner) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.homePlannerBase.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onPlannerClickListener(getItem(adapterPosition))
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(planner: BasePlanner) {
            binding.run {
                homeTvPlannerName.text = planner.title
                homeTvPlannerDate.text = "${planner.startDate.split("/")[1]}/${planner.startDate.split("/")[2]}~${planner.endDate.split("/")[1]}/${planner.endDate.split("/")[2]}"
                binding.homeTvPlannerMonth.text = "${planner.startDate.split("/")[1].toInt()}월"
            }
        }

    }

}