package ajou.paran.entrip.screen.community

import ajou.paran.entrip.databinding.ItemBoardImageListBinding
import ajou.paran.entrip.repository.network.dto.community.ResponseFindByIdPhoto
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BoardImageAdapter : ListAdapter<ResponseFindByIdPhoto, RecyclerView.ViewHolder>(
    BoardImageDiffCallback()
) {
    companion object{
        const val TAG = "[BoardImageAdapter]"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = BoardImageViewHolder(
        binding = ItemBoardImageListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    = (holder as BoardImageViewHolder).bind(getItem(position))

    fun setList(list: List<ResponseFindByIdPhoto>) {
        submitList(list)
        notifyDataSetChanged()
    }

    inner class BoardImageViewHolder
    constructor(
        private val binding: ItemBoardImageListBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ResponseFindByIdPhoto) {
            Glide.with(binding.root)
                .load(item.photoUrl)
                .override(300,300)
                .centerCrop()
                .into(binding.itemBoardImage)
        }

    }

}