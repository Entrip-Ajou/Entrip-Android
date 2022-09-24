package ajou.paran.entrip.screen.community.board

import ajou.paran.entrip.model.Comment
import androidx.recyclerview.widget.DiffUtil

class BoardCommentDiffCallback : DiffUtil.ItemCallback<Comment>() {

    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean
    = oldItem.comment.commentId == newItem.comment.commentId

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean
    = oldItem == newItem
}