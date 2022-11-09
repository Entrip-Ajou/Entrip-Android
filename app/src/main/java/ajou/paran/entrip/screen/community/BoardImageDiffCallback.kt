package ajou.paran.entrip.screen.community

import ajou.paran.entrip.repository.network.dto.community.ResponseFindByIdPhoto
import androidx.recyclerview.widget.DiffUtil

class BoardImageDiffCallback : DiffUtil.ItemCallback<ResponseFindByIdPhoto>() {

    override fun areItemsTheSame(
        oldItem: ResponseFindByIdPhoto,
        newItem: ResponseFindByIdPhoto
    ): Boolean
    = oldItem.photoId == newItem.photoId

    override fun areContentsTheSame(
        oldItem: ResponseFindByIdPhoto,
        newItem: ResponseFindByIdPhoto
    ): Boolean
    = oldItem == newItem

}