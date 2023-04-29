package dk.itu.moapd.scootersharing.ahga.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dk.itu.moapd.scootersharing.ahga.R
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity.Companion.fusedLocationProviderClient
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentMapsBinding

// GoogleMaps Key:
// https://console.cloud.google.com/welcome?project=moapd-2023-a42f2

class MapsFragment : Fragment(), OnMapReadyCallback {

    companion object {
        val itu = LatLng(55.6596, 12.5910)

        private const val ALL_PERMISSIONS_RESULT = 1011
        private val TAG = MainActivity :: class.qualifiedName
    }

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Cannot access binding because it is null. Is the view visible?"
    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         */

        // Check if the user allows the application to access the location-aware resources.
        if (!checkPermission())
            // Check if the user allows the application to access the location-aware resources.
            googleMap.isMyLocationEnabled = true

        //ITU marker
        googleMap.addMarker(MarkerOptions().position(itu).title("Marker at ITU"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itu, 15f))


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show a dialog to ask the user to allow the application to access the device's location.
        requestUserPermissions()

        if(!checkPermission()) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        Log.d(TAG, "HEJJ"+location.latitude.toString())
                    }

                    // Got last known location. In some rare situations this can be null.
                }
        }
    }

    private fun requestUserPermissions() {
        // An array with location-aware permissions.
        val permissions: ArrayList<String> = ArrayList()
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        // Check which permissions is needed to ask to the user.
        val permissionsToRequest = permissionsToRequest(permissions)

        // Show the permissions dialogs to the user.
        if (permissionsToRequest.size > 0)
            requestPermissions(
                permissionsToRequest.toTypedArray(),
                ALL_PERMISSIONS_RESULT
            )
    }

    private fun permissionsToRequest(permissions: ArrayList<String>): ArrayList<String> {
        val result: ArrayList<String> = ArrayList()
        for (permission in permissions)
            if (activity?.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                result.add(permission)
        return result
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestUserPermissions()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        // Check if the user allows the application to access the location-aware resources.
        if (checkPermission())
            return

        // Check if the user allows the application to access the location-aware resources.
        googleMap.isMyLocationEnabled = true

        // Set the default map type.
        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        // Setup the UI settings state.
        googleMap.uiSettings.apply {
            isCompassEnabled = true
            isIndoorLevelPickerEnabled = true
            isMyLocationButtonEnabled = true
            isRotateGesturesEnabled = true
            isScrollGesturesEnabled = true
            isTiltGesturesEnabled = true
            isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
        }

        // Move the Google Maps UI buttons under the OS top bar.
        googleMap.setPadding(0, 100, 0, 0)

    }

    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED

}