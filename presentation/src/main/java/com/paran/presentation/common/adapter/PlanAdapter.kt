package com.paran.presentation.common.adapter

import ajou.paran.domain.model.BasePlan
import ajou.paran.domain.model.BasePlanner
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paran.presentation.R
import com.paran.presentation.databinding.ItemPlanBinding
import com.paran.presentation.databinding.ItemPlanFooterBinding
import com.paran.presentation.utils.callbacks.BasePlanDiffCallback

class PlanAdapter(
    private val onClickDeletePlan: (plan: BasePlan) -> Unit,
    private val onClickPlan: (plan: BasePlan) -> Unit,
    private val onClickComment: (plan: BasePlan) -> Unit
) : ListAdapter<BasePlan, RecyclerView.ViewHolder>(BasePlanDiffCallback()) {
    companion object {
        private const val FOOTER_VIEW_TYPE = 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        FOOTER_VIEW_TYPE -> PlanFooterViewHolder(
            ItemPlanFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else -> PlanViewHolder(
            ItemPlanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClickDeletePlan = onClickDeletePlan,
            onClickPlan = onClickPlan,
            onClickComment = onClickComment
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {
            is PlanViewHolder -> {
                holder.bind(getItem(position))
            }
            is PlanFooterViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + 1

    override fun getItemViewType(position: Int): Int = when (position) {
        itemCount - 1 -> {
            FOOTER_VIEW_TYPE
        }
        else -> super.getItemViewType(position)
    }

    inner class PlanViewHolder(
        private val binding: ItemPlanBinding,
        private val onClickDeletePlan: (plan: BasePlan) -> Unit,
        private val onClickPlan: (plan: BasePlan) -> Unit,
        private val onClickComment: (plan: BasePlan) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.run {
                clPlan.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        onClickPlan(getItem(position))
                    }
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(plan: BasePlan) {
            binding.run {
                val (hour, minute) = timeSetUp(plan.time.toString())
                tvItemTime.text = "$hour:$minute"
                tvItemLocation.text = when (plan.location.isNullOrEmpty()) {
                    true -> ""
                    false -> plan.location
                }
                ivItemComment.setImageResource(
                    when (plan.isExistComments) {
                        true -> R.drawable.comment_event
                        false -> R.drawable.comment_default
                    }
                )
                ivItemDelete.setOnClickListener {
                    onClickDeletePlan(plan)
                }
                ivItemComment.setOnClickListener {
                    onClickComment(plan)
                }
                executePendingBindings()
            }
        }

        private fun timeSetUp(time: String): Pair<String, String> = when (time.length) {
            2 -> Pair("00", time)
            3 -> Pair("0${time.substring(0, 1)}", time.substring(time.length - 2, time.length))
            4 -> Pair(time.substring(0, 2), time.substring(time.length - 2, time.length))
            else -> Pair("00", "0$time")
        }
    }

    inner class PlanFooterViewHolder(
        private val binding: ItemPlanFooterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {

        }

    }

}