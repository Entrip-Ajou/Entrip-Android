package ajou.paran.entrip.screen.community.main

import ajou.paran.entrip.databinding.ItemLayoutRawCommunityBinding
import ajou.paran.entrip.repository.network.dto.community.ResponsePost
import ajou.paran.entrip.screen.community.board.BoardActivity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RawBoardAdapter : ListAdapter<ResponsePost, RecyclerView.ViewHolder>(RawBoardDiffCallback()) {

    private val _itemList: MutableList<ResponsePost> = mutableListOf()

    val itemList: List<ResponsePost>
        get() = _itemList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = RawBoardViewHolder(
        binding = ItemLayoutRawCommunityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    = (holder as RawBoardViewHolder).bind(getItem(position))

    fun setList(list: List<ResponsePost>) {
        _itemList.addAll(list)
        submitList(itemList)
        notifyDataSetChanged()
    }

    fun clearList() {
        _itemList.clear()
        submitList(_itemList)
        notifyDataSetChanged()
    }

    inner class RawBoardViewHolder
    constructor(
        private val binding: ItemLayoutRawCommunityBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ResponsePost) {
            binding.run {
                rawItemTitle.text = item.title
                rawItemContent.text = item.content
                rawItemLikeNum.text = item.likeNumber.toString()
                rawItemCommentNum.text = item.commentsNumber.toString()
                if (item.photoList.isNotEmpty()) {
                    Glide.with(binding.root)
                        .load(item.photoList[0])
                        .override(75,75)
                        .centerCrop()
                        .into(rawItemImage)
                }
                root.setOnClickListener {
                    binding.root.context.startActivity(
                        Intent(binding.root.context, BoardActivity::class.java).apply {
                            putExtra("postId", item.post_id)
                        }
                    )
                }
            }
        }

    }

}