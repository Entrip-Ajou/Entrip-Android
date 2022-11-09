package ajou.paran.entrip.screen.planner.top.notice

import ajou.paran.entrip.R
import ajou.paran.entrip.databinding.ItemLayoutNoticeBinding
import ajou.paran.entrip.repository.network.dto.NoticeResponse
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class NoticeAdapter(
    private val notices: MutableList<NoticeResponse>,
    val listener: RowClickListener,
    val user_id: String
) :
    RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    private var lock: Boolean = true

    inner class ViewHolder(
        private val itemBinding: ItemLayoutNoticeBinding,
        listener: RowClickListener
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(t: NoticeResponse) {
            if (lock) {
                itemBinding.noticeMark.visibility = View.VISIBLE
                lock = false
            }
            itemBinding.noticeText.text = t.title
            itemBinding.noticeUserName.text = t.nickname
            itemBinding.noticeTimestamp.text = t.modifiedDate

            if(user_id != t.author) itemBinding.noticeEtc.visibility = View.GONE

            itemBinding.noticeEtc.setOnClickListener {
                val array = arrayOf("수정", "삭제")
                val builder = AlertDialog.Builder(itemBinding.root.context)
                builder
                    .setItems(array, object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            if (p1 == 0) {
                                listener.onUpdateItemClickListener(t)
                            } else {
                                listener.onDeleteItemClickListener(t)
                            }
                        }
                    }).show()
            }


            itemBinding.itemNotice.setOnClickListener {
                listener.onItemClickListener(t)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeAdapter.ViewHolder {
        val v = ItemLayoutNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v, listener)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(notices[position])

    override fun getItemCount(): Int = notices.size

    fun update(list: List<NoticeResponse>) {
        notices.clear()
        notices.addAll(list)
        lock = true
        notifyDataSetChanged()
    }

    interface RowClickListener {
        fun onItemClickListener(noticeResponse: NoticeResponse)
        fun onUpdateItemClickListener(noticeResponse: NoticeResponse)
        fun onDeleteItemClickListener(noticeResponse: NoticeResponse)
    }
}