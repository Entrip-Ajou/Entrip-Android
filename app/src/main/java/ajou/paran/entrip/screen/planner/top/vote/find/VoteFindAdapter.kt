package ajou.paran.entrip.screen.planner.top.vote.find

import ajou.paran.entrip.databinding.ItemLayoutVotePostBinding
import ajou.paran.entrip.repository.network.dto.UsersAndContentsReturnDto
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class VoteFindAdapter(
    private val votes : MutableList<UsersAndContentsReturnDto>
) : RecyclerView.Adapter<VoteFindAdapter.ViewHolder>() {

    var selectedList = mutableListOf<Boolean>()

    private var anonymousVote = false
    private var multipleVotes = false
    private var voting = false

    inner class ViewHolder(
        private val itemBinding: ItemLayoutVotePostBinding,
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(t: UsersAndContentsReturnDto, position: Int) {
            itemBinding.radiobtnContent.isChecked = selectedList[position]
            itemBinding.radiobtnContent.text = t.content
            itemBinding.tvVotePerson.text = t.users.size.toString() + "명"

            if(!voting){
                itemBinding.radiobtnContent.setOnClickListener {
                    if(multipleVotes){
                        selectedList[position] = !selectedList[position]
                        itemBinding.radiobtnContent.isChecked = selectedList[position]

                    } else {
                        selectedList[position] = !selectedList[position]

                        if(selectedList[position]){
                            for(i in 0 until votes.size){
                                if(i != position) {
                                    selectedList[i] = false
                                    notifyItemChanged(i)
                                }
                            }
                            itemBinding.radiobtnContent.isChecked = selectedList[position]

                        }else{
                            itemBinding.radiobtnContent.isChecked = selectedList[position]
                        }
                    }
                }
            }

            if(!anonymousVote){
                itemBinding.tvVotePerson.setOnClickListener {
                    // 추후에 다시 개발
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteFindAdapter.ViewHolder {
        val v = ItemLayoutVotePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        selectedList = mutableListOf<Boolean>()
        for(i in 0 until votes.size) selectedList.add(false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: VoteFindAdapter.ViewHolder, position: Int) = holder.bind(votes[position], position)

    override fun getItemCount(): Int = votes.size

    fun configuration(anonymousVote: Boolean, multipleVotes: Boolean, voting: Boolean){
        this.anonymousVote = anonymousVote
        this.multipleVotes = multipleVotes
        this.voting = voting
    }

    fun update(list: List<UsersAndContentsReturnDto>) {
        votes.clear()
        votes.addAll(list)
        notifyDataSetChanged()
    }

    fun mappingIndex(content_Id : Long) : Long {
        val first_id = votes.get(0).contentId
        return content_Id - first_id
    }
}