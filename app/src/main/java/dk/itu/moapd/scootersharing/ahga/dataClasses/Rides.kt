package dk.itu.moapd.scootersharing.ahga.dataClasses;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
data class Rides(
    var scooter: String = "unknown",
    val price: Double = 0.0,
    val startLocation: String = "",
    val startTime: Long? = null,
    val endTime: Long? = null,
) {

}