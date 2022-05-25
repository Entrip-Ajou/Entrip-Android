package ajou.paran.entrip.screen.planner.top.useradd

import ajou.paran.entrip.databinding.ItemLayoutWaitingBinding
import ajou.paran.entrip.model.WaitEntity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class WaitingAdapter() : ListAdapter<WaitEntity, RecyclerView.ViewHolder>(WaitDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = ItemLayoutWaitingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WaitViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int){
        (holder as WaitViewHolder).bind(getItem(position))
    }

    inner class WaitViewHolder(private val itemBinding : ItemLayoutWaitingBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(t : WaitEntity){
            Glide.with(itemView)
                .load(t.photoUrl)
                .into(itemBinding.imageProfile)

            itemBinding.tvUserOrNickname.text = t.nickname
        }
    }

    override fun getItemCount(): Int = super.getItemCount()

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}

class WaitDiffCallback : DiffUtil.ItemCallback<WaitEntity>(){
    override fun areItemsTheSame(oldItem: WaitEntity, newItem: WaitEntity): Boolean {
        return oldItem.wait_id == newItem.wait_id
    }

    override fun areContentsTheSame(oldItem: WaitEntity, newItem: WaitEntity): Boolean {
        return oldItem == newItem
    }
}