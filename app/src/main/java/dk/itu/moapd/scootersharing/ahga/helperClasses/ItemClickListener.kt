package dk.itu.moapd.scootersharing.ahga.helperClasses

import dk.itu.moapd.scootersharing.ahga.dataClasses.Scooter


/**
 * An interface to implements listener methods for `RecyclerView` items.
 */
interface ItemClickListener  {

    /**
     * Implement this method to be executed when the user press an item in the `RecyclerView` for a
     * long time.
     *
     * @param scooter An instance of `Scooter` class.
     * @param position The selected position in the `RecyclerView`.
     */
    fun onItemClickListener(scooter: Scooter, position: Int)

}
