package com.paran.presentation.utils.callbacks

import ajou.paran.domain.model.PlannerDate
import androidx.recyclerview.widget.DiffUtil

class PlannerDateDiffCallback : DiffUtil.ItemCallback<PlannerDate>() {

    override fun areItemsTheSame(oldItem: PlannerDate, newItem: PlannerDate): Boolean = oldItem.date == newItem.date

    override fun areContentsTheSame(oldItem: PlannerDate, newItem: PlannerDate): Boolean = oldItem == newItem

}