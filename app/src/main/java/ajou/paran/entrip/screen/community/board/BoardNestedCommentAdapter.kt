package ajou.paran.entrip.screen.community.board

import ajou.paran.entrip.databinding.ItemLayoutBoardNestedCommentBinding
import ajou.paran.entrip.repository.network.dto.community.ResponseNestedComment
import ajou.paran.entrip.util.SingleLiveEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BoardNestedCommentAdapter : ListAdapter<ResponseNestedComment, RecyclerView.ViewHolder>(BoardNestedCommentDiffCallback()) {

    private val _nestedCommentList: MutableList<ResponseNestedComment> = mutableListOf()

    val nestedCommentList: List<ResponseNestedComment>
        get() = _nestedCommentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = BoardNestedCommentViewHolder(
        binding = ItemLayoutBoardNestedCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    = (holder as BoardNestedCommentViewHolder).bind(getItem(position))

    fun setList(list: List<ResponseNestedComment>) {
        _nestedCommentList.clear()
        _nestedCommentList.addAll(list)
        submitList(_nestedCommentList)
        notifyDataSetChanged()
    }

    inner class BoardNestedCommentViewHolder
    constructor(
        val binding: ItemLayoutBoardNestedCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseNestedComment) {
            binding.run {
                tvNickname.text = item.nickname
                tvComment.text = item.content
            }
        }
    }

}