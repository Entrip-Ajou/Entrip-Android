package ajou.paran.entrip.screen.planner.mid.comment


import ajou.paran.entrip.databinding.ItemLayoutCommentBinding
import ajou.paran.entrip.repository.network.dto.CommentResponse
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CommentAdapter
constructor(
    val listener: CommentItemClickListener
) : ListAdapter<CommentResponse, RecyclerView.ViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CommentViewHolder(ItemLayoutCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CommentViewHolder).bind(getItem(position))
    }

    override fun getItemCount(): Int = super.getItemCount()

    inner class CommentViewHolder(
        private val binding: ItemLayoutCommentBinding,
        listener: CommentItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                commentLayout.setOnLongClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCommentLongClickListener(getItem(position))
                    }
                    true
                }
            }
        }

        fun bind(commentResponse: CommentResponse){
            binding.tvNickname.text = commentResponse.nickname
            binding.tvComment.text = commentResponse.content
            Glide.with(itemView)
                .load(commentResponse.photoUrl)
                .into(binding.imageProfile)
        }
    }

    interface CommentItemClickListener {
        fun onCommentLongClickListener(commentResponse: CommentResponse)
    }
}

class CommentDiffCallback : DiffUtil.ItemCallback<CommentResponse>() {
    override fun areItemsTheSame(oldItem: CommentResponse, newItem: CommentResponse): Boolean {
        return oldItem.comment_id == newItem.comment_id
    }

    override fun areContentsTheSame(oldItem: CommentResponse, newItem: CommentResponse): Boolean {
        return oldItem == newItem
    }

}