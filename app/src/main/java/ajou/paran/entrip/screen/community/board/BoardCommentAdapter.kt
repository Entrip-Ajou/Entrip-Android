package ajou.paran.entrip.screen.community.board

import ajou.paran.entrip.databinding.ItemLayoutBoardCommentBinding
import ajou.paran.entrip.model.Comment
import ajou.paran.entrip.repository.network.dto.community.ResponseComment
import ajou.paran.entrip.repository.network.dto.community.ResponseNestedComment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BoardCommentAdapter : ListAdapter<Comment, RecyclerView.ViewHolder>(BoardCommentDiffCallback()) {

    private val _commentList: MutableList<Comment> = mutableListOf()

    private lateinit var boardNestedCommentAdapter: BoardNestedCommentAdapter

    val commentList: List<Comment>
        get() = _commentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = BoardCommentViewHolder(
        binding = ItemLayoutBoardCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    = (holder as BoardCommentViewHolder).bind(getItem(position))

    fun setList(list: List<Comment>) {
        _commentList.clear()
        _commentList.addAll(list)
        submitList(_commentList)
        notifyDataSetChanged()
    }

    inner class BoardCommentViewHolder
    constructor(
        private val binding: ItemLayoutBoardCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Comment) {
            binding.run {
                tvNickname.text = item.comment.nickname
                tvComment.text = item.comment.content

                boardNestedCommentAdapter = BoardNestedCommentAdapter()
                nestedComment.run {
                    adapter = boardNestedCommentAdapter
                    layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
                }
                boardNestedCommentAdapter.setList(item.listNestedComment)
            }
        }
    }
}