package dk.itu.moapd.scootersharing.ahga.dataClasses

import android.content.Context
import java.util.Random
import kotlin.collections.ArrayList

class RidesDB private constructor(context: Context) {
    private val rides = ArrayList<Scooter>()

    companion object : RidesDBHolder<RidesDB, Context>(::RidesDB)

    init {
        rides.add(
            Scooter("CPH001", "ITU ", randomDate())
        )
        rides.add(
            Scooter("CPH002", "Fields ", randomDate())
        )
        rides.add(
            Scooter("CPH003", "Lufthavn ", randomDate())
        )
        rides.add(
            Scooter("CPH004", "ITU ", randomDate())
        )
        rides.add(
            Scooter("CPH005", "Fields ", randomDate())
        )
        rides.add(
            Scooter("CPH006", "Lufthavn ", randomDate())
        )
        rides.add(
            Scooter("CPH007", "ITU ", randomDate())
        )
        rides.add(
            Scooter("CPH008", "Fields ", randomDate())
        )
        rides.add(
            Scooter("CPH009", "Lufthavn ", randomDate())
        )
        rides.add(
            Scooter("CPH010", "ITU ", randomDate())
        )
        rides.add(
            Scooter("CPH011", "Fields ", randomDate())
        )
        rides.add(
            Scooter("CPH012", "Lufthavn ", randomDate())
        )
        rides.add(
            Scooter("CPH013", "ITU ", randomDate())
        )
        rides.add(
            Scooter("CPH014", "Fields ", randomDate())
        )
        rides.add(
            Scooter("CPH015", "Lufthavn ", randomDate())
        )
    }

    fun getRidesList(): List<Scooter> {
        return rides
    }

    fun addScooter(name: String, location: String) {
        rides.add(
            Scooter(name, location)
        )
    }

    fun containsScooter(name: String) : Boolean {
        return rides.contains(rides.find { it.name == name })
    }

    fun deleteScooterByName(name: String) {
        val scooterToRemove = rides.find { it.name == name }
        scooterToRemove?.let { rides.remove(it) }
    }

    fun deleteScooterByID(itemID: Int) {
        if (rides.size != 0){
            rides.removeAt(itemID)
        }
    }

    fun updateCurrentScooter(location: String) {
        rides.last().location = location
        rides.last().timestamp = System.currentTimeMillis()
    }

    fun getCurrentScooter(): Scooter {
        return rides.last()
    }

    fun getCurrentScooterInfo(): String {
      return getCurrentScooter().toString()
    }

    /**
     * Generate a random timestamp in the last 365 days .
     *
     * @return A random timestamp in the last year .
     */
    private fun randomDate(): Long {
        val random = Random()
        val now = System.currentTimeMillis()
        val year = random.nextDouble() * 1000 * 60 * 60 * 24 * 365
        return (now - year).toLong()
    }
}

open class RidesDBHolder<out T : Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile
    private var instance: T? = null
    fun get(arg: A): T {
        val checkInstance = instance
        if (checkInstance != null)
            return checkInstance
        return synchronized(this) {
            val checkInstanceAgain = instance
            if (checkInstanceAgain != null)
                checkInstanceAgain
            else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}