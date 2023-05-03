package dk.itu.moapd.scootersharing.ahga.dataClasses;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
data class Rides(
    var scooter: Scooter = Scooter(),
    val price: Double? = (Math.random() * 100),
    val startLatitude: Double? = 0.0,
    val startLongitude: Double? = 0.0,
    val endLatitude: Double? = 0.0,
    val endLongitude: Double? = 0.0,
    val startTime: Long? = null,
    val endTime: Long? = null,
) {

}