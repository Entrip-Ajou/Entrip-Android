package ajou.paran.entrip.screen.planner.main

import ajou.paran.entrip.databinding.ItemLayoutInviteBinding
import ajou.paran.entrip.model.InviteEntity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class InviteAdapter(val listener: TextViewClickListner) : ListAdapter<InviteEntity, RecyclerView.ViewHolder>(InviteDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemLayoutInviteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InviteViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InviteViewHolder).bind(getItem(position))
    }

    override fun getItemCount(): Int = super.getItemCount()

    override fun getItemViewType(position: Int): Int = position

    inner class InviteViewHolder(private val itemBinding: ItemLayoutInviteBinding, listener: TextViewClickListner)
        :RecyclerView.ViewHolder(itemBinding.root){
            fun bind(t : InviteEntity){
                Glide.with(itemView)
                    .load(t.photoUrl)
                    .into(itemBinding.imageProfile)

                itemBinding.tvNickname.text = t.nickname
                itemBinding.tvPlannerTitle.text = t.planner_title
                itemBinding.tvInviteAccept.setOnClickListener {
                    listener.onAcceptInvitation(t)
                }
                itemBinding.tvInviteReject.setOnClickListener {
                    listener.onRejectInvitation(t)
                }
            }
        }

    interface TextViewClickListner{
        fun onAcceptInvitation(inviteEntity: InviteEntity)
        fun onRejectInvitation(inviteEntity: InviteEntity)
    }
}

class InviteDiffCallback : DiffUtil.ItemCallback<InviteEntity>(){
    override fun areItemsTheSame(oldItem: InviteEntity, newItem: InviteEntity): Boolean {
        return oldItem.invite_id == newItem.invite_id
    }

    override fun areContentsTheSame(oldItem: InviteEntity, newItem: InviteEntity): Boolean {
        return oldItem == newItem
    }

}