package ajou.paran.entrip.screen.planner.top.useradd

import ajou.paran.entrip.databinding.ItemLayoutSharingBinding
import ajou.paran.entrip.repository.network.dto.SharingFriend
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import javax.inject.Inject

class SharingAdapter(private val users: MutableList<SharingFriend>) :
    RecyclerView.Adapter<SharingAdapter.ViewHolder>() {

    inner class ViewHolder(private val itemBinding: ItemLayoutSharingBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(t: SharingFriend) {
            Glide.with(itemView)
                .load(t.photoUrl)
                .into(itemBinding.imgItemProfile)

            itemBinding.tvItemNickname.text = t.nickname
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemLayoutSharingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(users[position])

    override fun getItemCount(): Int = users.size

    fun update(list: List<SharingFriend>) {
        users.clear()
        users.addAll(list)
        notifyDataSetChanged()
    }
}