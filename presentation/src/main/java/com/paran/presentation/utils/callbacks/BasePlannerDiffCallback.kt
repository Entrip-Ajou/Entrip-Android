package com.paran.presentation.utils.callbacks

import ajou.paran.domain.model.BasePlanner
import androidx.recyclerview.widget.DiffUtil

class BasePlannerDiffCallback : DiffUtil.ItemCallback<BasePlanner>() {

    override fun areItemsTheSame(oldItem: BasePlanner, newItem: BasePlanner): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: BasePlanner, newItem: BasePlanner): Boolean = oldItem == newItem

}