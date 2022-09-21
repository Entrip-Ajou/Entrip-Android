package ajou.paran.entrip.screen.community.board

import ajou.paran.entrip.repository.network.dto.community.ResponseComment
import androidx.recyclerview.widget.DiffUtil

class BoardCommentDiffCallback : DiffUtil.ItemCallback<ResponseComment>() {

    override fun areItemsTheSame(oldItem: ResponseComment, newItem: ResponseComment): Boolean
    = oldItem.commentId == newItem.commentId

    override fun areContentsTheSame(oldItem: ResponseComment, newItem: ResponseComment): Boolean
    = oldItem == newItem
}