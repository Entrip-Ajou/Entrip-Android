package ajou.paran.entrip.screen.planner.top.vote

import ajou.paran.entrip.databinding.ItemLayoutVoteBinding
import ajou.paran.entrip.repository.network.dto.VoteResponse
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class VoteFinishAdapter(private val votes : MutableList<VoteResponse>, val listener : VotingAdapter.RowClickListener) :
    RecyclerView.Adapter<VoteFinishAdapter.ViewHolder>(){

    inner class ViewHolder(
        private val itemBinding: ItemLayoutVoteBinding,
        listener: VotingAdapter.RowClickListener
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(t: VoteResponse) {
            if(!t.voting){
                itemBinding.voteTitle.text = t.title

                itemBinding.voteEtc.setOnClickListener {
                    val array = arrayOf("수정", "삭제")
                    val builder = AlertDialog.Builder(itemBinding.root.context)
                    builder
                        .setItems(array, object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                if(p1 == 0){
                                    listener.onUpdateItemClickListener(t)
                                }else{
                                    listener.onDeleteItemClickListener(t)
                                }
                            }
                        }).show()
                }

                itemBinding.itemVote.setOnClickListener{
                    listener.onItemClickListener(t)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteFinishAdapter.ViewHolder {
        val v = ItemLayoutVoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v, listener)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(votes[position])

    override fun getItemCount(): Int = votes.size

    fun update(list: List<VoteResponse>) {
        votes.clear()
        votes.addAll(list)
        notifyDataSetChanged()
    }
}
