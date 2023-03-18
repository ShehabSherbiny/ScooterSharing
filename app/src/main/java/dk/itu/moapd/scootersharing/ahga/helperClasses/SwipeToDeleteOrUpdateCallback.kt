package dk.itu.moapd.scootersharing.ahga.helperClasses

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity.Companion.ridesDB

class SwipeToDeleteOrUpdateCallback(private val adapter: RecyclerView.Adapter<*>) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition

        when (direction) {
            ItemTouchHelper.LEFT -> {
                ridesDB.deleteScooterByID(position)
                adapter.notifyItemRemoved(position)
            }
            ItemTouchHelper.RIGHT -> {
                //TODO: call update ride
                //findNavController().navigate(R.id.show_update_ride_fragment)
                adapter.notifyItemChanged(position)
            }
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }
}
