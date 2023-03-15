package dk.itu.moapd.scootersharing.ahga

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDeleteOrUpdateCallback(private val adapter: RecyclerView.Adapter<*>) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        when (direction) {
            ItemTouchHelper.LEFT -> {
                //TODO: delete from rides array for real
                adapter.notifyItemRemoved(position)
            }
            ItemTouchHelper.RIGHT -> {
                //TODO: call update ride
                adapter.notifyItemChanged(position)
            }
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }
}
