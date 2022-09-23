package ajou.paran.entrip.screen.community.board

import ajou.paran.entrip.databinding.ItemLayoutBoardCommentBinding
import ajou.paran.entrip.repository.network.dto.community.ResponseComment
import ajou.paran.entrip.repository.network.dto.community.ResponseNestedComment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BoardCommentAdapter : ListAdapter<ResponseComment, RecyclerView.ViewHolder>(BoardCommentDiffCallback()) {

    private val _commentList: MutableList<ResponseComment> = mutableListOf()

    val commentList: List<ResponseComment>
        get() = _commentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = BoardCommentViewHolder(
        binding = ItemLayoutBoardCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val boardCommentViewHolder = holder as BoardCommentViewHolder
        boardCommentViewHolder.bind(getItem(position))
    }

    fun setList(list: List<ResponseComment>) {
        _commentList.clear()
        _commentList.addAll(list)
        submitList(_commentList)
        notifyDataSetChanged()
    }

    inner class BoardCommentViewHolder
    constructor(
        private val binding: ItemLayoutBoardCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseComment) {
            binding.run {
                tvNickname.text = item.nickname
                tvComment.text = item.content
            }
        }

        fun setChildAdapter(item: ResponseComment, childList: List<ResponseNestedComment>) {

        }
    }
}