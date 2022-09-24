package ajou.paran.entrip.screen.community

import ajou.paran.entrip.databinding.ItemBoardImageListBinding
import ajou.paran.entrip.repository.network.dto.community.ResponseFindByIdPhoto
import ajou.paran.entrip.screen.community.board.BoardActivity
import ajou.paran.entrip.screen.community.select.ImageSelectActivity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

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
            val circularProgressDrawable = CircularProgressDrawable(binding.root.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(binding.root)
                .load(item.photoUrl)
                .placeholder(circularProgressDrawable)
                .override(300,300)
                .centerCrop()
                .into(binding.itemBoardImage)

            binding.itemBoardImage.setOnClickListener {
                Log.d(TAG, "TODO: Image Click Event")
                binding.root.context.startActivity(
                    Intent(
                        binding.root.context,
                        ImageSelectActivity::class.java
                    ).apply {
                        putExtra("photoUrl", item.photoUrl)
                    }
                )
            }
        }

    }

}