package ajou.paran.entrip.screen.community.main

import ajou.paran.entrip.repository.network.dto.community.ResponsePost
import androidx.recyclerview.widget.DiffUtil

class RawBoardDiffCallback : DiffUtil.ItemCallback<ResponsePost>() {

    override fun areItemsTheSame(oldItem: ResponsePost, newItem: ResponsePost): Boolean
    = oldItem.post_id == newItem.post_id

    override fun areContentsTheSame(oldItem: ResponsePost, newItem: ResponsePost): Boolean
    = oldItem == newItem

}