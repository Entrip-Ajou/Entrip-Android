package ajou.paran.entrip.screen.planner.top.useradd

import ajou.paran.entrip.databinding.ItemLayoutSharingBinding
import ajou.paran.entrip.databinding.ItemLayoutWaitingBinding
import ajou.paran.entrip.model.WaitEntity
import ajou.paran.entrip.repository.network.dto.SharingFriend
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class WaitingAdapter (private val waits: MutableList<WaitEntity>) : RecyclerView.Adapter<WaitingAdapter.ViewHolder>(){

    inner class ViewHolder(private val itemBinding : ItemLayoutWaitingBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(t : WaitEntity){
            Glide.with(itemView)
                .load(t.photoUrl)
                .into(itemBinding.imageProfile)

            itemBinding.tvUserOrNickname.text = t.nickname
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemLayoutWaitingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(waits[position])

    override fun getItemCount(): Int = waits.size

    fun update(list:List<WaitEntity>){
        waits.clear()
        waits.addAll(list)
        notifyDataSetChanged()
    }
}