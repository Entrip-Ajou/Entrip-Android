package ajou.paran.entrip.screen.community.select

import ajou.paran.entrip.R
import ajou.paran.entrip.model.Media
import ajou.paran.entrip.util.*
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.fetch.VideoFrameUriFetcher
import coil.load

class MediaAdapter(
    private val onItemClick: (Boolean, Uri) -> Unit,
    private val onDeleteClick: (Boolean, Uri) -> Unit,
) : ListAdapter<Media, MediaAdapter.PicturesViewHolder>(MediaDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PicturesViewHolder(parent.context.layoutInflater.inflate(R.layout.item_picture, parent, false))

    override fun onBindViewHolder(holder: PicturesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun shareImage(currentPage: Int, action: (Media) -> Unit) {
        if (currentPage < itemCount) {
            action(getItem(currentPage))
        }
    }

    fun deleteImage(currentPage: Int) {
        if (currentPage < itemCount) {
            val media = getItem(currentPage)
            val allMedia = currentList.toMutableList()
            allMedia.removeAt(currentPage)
            submitList(allMedia)
            onDeleteClick(allMedia.size == 0, media.uri)
        }
    }

    inner class PicturesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagePreview: ImageView = itemView.findViewById(R.id.imagePreview)
        private val imagePlay: ImageView = itemView.findViewById(R.id.imagePlay)

        fun bind(item: Media) {
            imagePlay.visibility = if (item.isVideo) View.VISIBLE else View.GONE
            imagePreview.load(item.uri) {
                if (item.isVideo) {
                    fetcher(VideoFrameUriFetcher(itemView.context))
                }
            }
            imagePreview.setOnClickListener { onItemClick(item.isVideo, item.uri) }
        }
    }
}