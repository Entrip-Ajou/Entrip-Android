package ajou.paran.entrip.util.ui

import ajou.paran.entrip.R
import ajou.paran.entrip.screen.planner.mid.PlanAdapter
import android.graphics.Canvas
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

class SwipeHelperCallback(
    private val recyclerViewAdapter: PlanAdapter
): ItemTouchHelper.Callback() {

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int
        = makeMovementFlags(0, LEFT or RIGHT)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean
        = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentDx = 0f
        previousPosition = viewHolder.adapterPosition
        getDefaultUIUtil().clearView(getView(viewHolder))
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            currentPosition = viewHolder.adapterPosition
            getDefaultUIUtil().onSelected(getView(it))
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ACTION_STATE_SWIPE){
            val view = getView(viewHolder)
            val isClamped = getTag(viewHolder)
            val newX = clampViewPositionHorizontal(dX, isClamped, isCurrentlyActive)

            if(newX == -clamp){
                getView(viewHolder).animate().translationX(-clamp).setDuration(100L).start()
                return
            }
            if (dX == 0f){
                getDeleteView(viewHolder).findViewById<LinearLayout>(R.id.ll_item_delete).visibility = View.INVISIBLE
            }else{
                getDeleteView(viewHolder).findViewById<LinearLayout>(R.id.ll_item_delete).visibility = View.VISIBLE
            }

            currentDx = newX
            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                view,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )

        }
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float
        = defaultValue * 10

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        setTag(viewHolder, currentDx*2 <= -clamp)
        return 2f
    }

    fun setClamp(clamp: Float){
        this.clamp = clamp
    }

    fun removePreviousClamp(recyclerView: RecyclerView){
        if(currentPosition == previousPosition)
            return

        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).animate().x(0f).setDuration(100L).start()
            getDeleteView(viewHolder).findViewById<LinearLayout>(R.id.ll_item_delete).visibility = View.INVISIBLE
            setTag(viewHolder, false)
            previousPosition = null
        }
    }

    fun removeMyClamp(recyclerView: RecyclerView){
        if(currentPosition == previousPosition){
            previousPosition?.let {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
                getView(viewHolder).animate().x(0f).setDuration(100L).start()
                getDeleteView(viewHolder).findViewById<LinearLayout>(R.id.ll_item_delete).visibility = View.INVISIBLE
                setTag(viewHolder, false)
                previousPosition = null
            }
        }
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View
        = viewHolder.itemView.findViewById(R.id.itemClick)

    private fun getDeleteView(viewHolder: RecyclerView.ViewHolder): View
            = viewHolder.itemView.findViewById(R.id.ll_item_delete)

    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean){
        viewHolder.itemView.tag = isClamped
    }

    private fun getTag(viewHolder: RecyclerView.ViewHolder): Boolean
        = viewHolder.itemView.tag as? Boolean ?: false

    private fun clampViewPositionHorizontal(
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ): Float{
        val max = 0f
        val newX = if (isClamped)
            if (isCurrentlyActive)
                if (dX < 0)
                    // swipe right
                    dX/3 - clamp
                else
                    // swipe left
                    dX - clamp
            else
                -clamp
        else
            dX / 2

        return min(newX, max)
    }

}