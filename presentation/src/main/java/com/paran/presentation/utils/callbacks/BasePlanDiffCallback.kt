package com.paran.presentation.utils.callbacks

import ajou.paran.domain.model.BasePlan
import androidx.recyclerview.widget.DiffUtil

class BasePlanDiffCallback : DiffUtil.ItemCallback<BasePlan>() {

    override fun areItemsTheSame(oldItem: BasePlan, newItem: BasePlan): Boolean = oldItem.planId == newItem.planId

    override fun areContentsTheSame(oldItem: BasePlan, newItem: BasePlan): Boolean = oldItem == newItem

}