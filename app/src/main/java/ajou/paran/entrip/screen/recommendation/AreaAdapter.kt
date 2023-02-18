package ajou.paran.entrip.screen.recommendation

import ajou.paran.entrip.databinding.ItemLayoutAreaBinding
import ajou.paran.entrip.screen.recommendation.model.AreaRecord
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AreaAdapter(
    private val area: MutableList<AreaRecord>,
    private val onRatingChangedListener: OnRatingChangedListener,
    private var index : Int
)
    : RecyclerView.Adapter<AreaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaAdapter.ViewHolder {
        val v = ItemLayoutAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AreaAdapter.ViewHolder, position: Int) = holder.bind(area[position])

    override fun getItemCount(): Int = area.size

    inner class ViewHolder(private val itemBinding : ItemLayoutAreaBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
                fun bind(t : AreaRecord) {
                    itemBinding.tvDistricts.text = t.area
                    itemBinding.rating.rating = t.rating.toFloat()

                    itemBinding.rating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                        onRatingChangedListener.onRatingChanged(adapterPosition, rating.toInt(), index)
                    }

                }
            }

    fun update(list : List<AreaRecord>, _index : Int) {
        area.clear()
        area.addAll(list)
        index = _index
        notifyDataSetChanged()
    }

    interface OnRatingChangedListener {
        fun onRatingChanged(position: Int, rating: Int, index : Int)
    }

}