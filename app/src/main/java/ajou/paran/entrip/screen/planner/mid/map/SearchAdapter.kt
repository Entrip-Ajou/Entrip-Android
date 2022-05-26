package ajou.paran.entrip.screen.planner.mid.map

import ajou.paran.entrip.databinding.ItemLayoutLocationBinding
import ajou.paran.entrip.repository.network.dto.Place
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(private val searchs : MutableList<Place>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private lateinit var listener : RowCLickListener
    interface RowCLickListener{
        fun onItemClickListener(place : Place)
    }
    fun setOnItemClickListener(listener: RowCLickListener){
        this.listener = listener
    }

    inner class ViewHolder(val itemBinding : ItemLayoutLocationBinding, val listener: RowCLickListener) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(t : Place){
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemBinding.tvPlace.text = t.place_name
                itemBinding.tvAddress.text = t.road_address_name

                itemView.setOnClickListener {
                    listener?.onItemClickListener(t)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemLayoutLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v, listener)
    }

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(searchs[position])

    override fun getItemCount() = searchs.size

    fun update(list : List<Place>){
        searchs.clear()
        searchs.addAll(list)
        notifyDataSetChanged()
    }
}