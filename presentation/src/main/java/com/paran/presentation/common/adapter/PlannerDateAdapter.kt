package com.paran.presentation.common.adapter

import ajou.paran.domain.model.PlannerDate
import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paran.presentation.databinding.ItemPlannerDateBinding
import com.paran.presentation.utils.callbacks.PlannerDateDiffCallback

class PlannerDateAdapter
constructor(
    private val onClick: (date: PlannerDate) -> Unit
) : ListAdapter<PlannerDate, RecyclerView.ViewHolder>(PlannerDateDiffCallback()) {

    private var selectedItemPos = 0
    private var lastItemSelectedPos = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = DateViewHolder(
        ItemPlannerDateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        onClick = onClick
    )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (position) {
            selectedItemPos -> (holder as DateViewHolder).selected()
            else -> (holder as DateViewHolder).unSelected()
        }
        holder.bind(getItem(position))
    }

    override fun submitList(list: MutableList<PlannerDate>?) {
        selectedItemPos = 0
        lastItemSelectedPos = 0
        super.submitList(list)
    }

    inner class DateViewHolder(
        private val binding: ItemPlannerDateBinding,
        private val onClick: (date: PlannerDate) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(date: PlannerDate) {
            val (year, month, day) = date.date.split("/")
            binding.itemLayoutTvMonth.text = "${month.toInt()}월"
            binding.itemLayoutTvDay.text = day
            binding.clDate.setOnClickListener {
                lastItemSelectedPos = selectedItemPos
                selectedItemPos = adapterPosition

                if (lastItemSelectedPos != selectedItemPos) {
                    notifyItemChanged(lastItemSelectedPos)
                    notifyItemChanged(selectedItemPos)
                    onClick(date)
                }
            }
        }

        fun unSelected() {
            binding.itemLayoutTvMonth.setTextColor(Color.parseColor("#7a7a7a"))
            binding.itemLayoutTvDay.setTextColor(Color.parseColor("#7a7a7a"))
        }

        fun selected() {
            binding.itemLayoutTvMonth.setTextColor(Color.parseColor("#2d95eb"))
            binding.itemLayoutTvDay.setTextColor(Color.parseColor("#2d95eb"))
        }

    }

}