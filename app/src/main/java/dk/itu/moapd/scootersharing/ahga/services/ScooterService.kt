package dk.itu.moapd.scootersharing.ahga.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity.Companion.fusedLocationProviderClient

class ScooterService : Service() {

    companion object {
        private const val TAG = "SCOOTER SERVICE"
        var currentLatitude = 55.6596
        var currentLongitude =12.5910

    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            // Updates the user interface components with GPS data location.
            locationResult.lastLocation?.let { location ->
             /*   //LAST LOCATION
                Toast.makeText(
                    this@ScooterService,
                    "SCOOTER SERVICE: LOCATION" + location,
                    Toast.LENGTH_SHORT
                ).show()*/

                currentLatitude = location.latitude
                currentLongitude= location.longitude

            }
        }
    }

    override fun onCreate() {

        // Start receiving location updates.
        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(this)

        subscribeToLocationUpdates()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
        unsubscribeToLocationUpdates()
    }

    private fun subscribeToLocationUpdates() {

        // Check if the user allows the application to access the location-aware resources.
        if (checkPermission())
            return

        // Sets the accuracy and desired interval for active location updates.
        val locationRequest = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, 5)
            .build()

        // Subscribe to location changes.
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    private fun unsubscribeToLocationUpdates() {
        // Unsubscribe to location changes.
        fusedLocationProviderClient
            .removeLocationUpdates(locationCallback)
    }


    // PERMISSIONS
    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED


}