package ajou.paran.entrip.screen.community.board

import ajou.paran.entrip.repository.network.dto.community.ResponseNestedComment
import androidx.recyclerview.widget.DiffUtil

class BoardNestedCommentDiffCallback : DiffUtil.ItemCallback<ResponseNestedComment>() {

    override fun areItemsTheSame(
        oldItem: ResponseNestedComment,
        newItem: ResponseNestedComment
    ): Boolean
    = oldItem.nestedCommentId == newItem.nestedCommentId

    override fun areContentsTheSame(
        oldItem: ResponseNestedComment,
        newItem: ResponseNestedComment
    ): Boolean
    = oldItem == newItem

}