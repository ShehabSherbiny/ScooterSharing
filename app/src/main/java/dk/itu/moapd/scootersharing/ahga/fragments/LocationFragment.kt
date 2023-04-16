package dk.itu.moapd.scootersharing.ahga.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentLocationBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private const val TAG = "LocationFragment"

class LocationFragment : Fragment() {

    companion object {
        private const val ALL_PERMISSIONS_RESULT = 1011
        private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
        private lateinit var locationCallback: LocationCallback
    }

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Cannot access binding because it is null. Is the view visible?"
    }
//    private val viewModel: ScooterSharingVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment.
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        subscribeToLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        unsubscribeToLocationUpdates()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startLocationAware()
    }

    private fun startLocationAware() {

        // Show a dialog to ask the user to allow the application to access the device's location.
        requestUserPermissions()

        // Start receiving location updates.
        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(requireContext())

        // Initialize the `LocationCallback`.
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

//              viewModel.onLocationChanged(locationResult.lastLocation)
                locationResult.lastLocation?.let { location ->
//                  binding.latitudeTextField.editText?.setText(location.latitude.toString())
                    updateUI(location)
                }

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
//            TODO: uncomment this line
//            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                result.add(permission)
        return result
    }

    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED

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

    private fun Long.toDateString() : String {
        val date = Date(this)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }

    private fun updateUI(location: Location) {
//        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            binding.apply {
                latitudeTextField?.editText?.setText(location.latitude.toString())
                longitudeTextField?.editText?.setText(location.longitude.toString())
                timeTextField?.editText?.setText(location.time.toDateString())
            }
//        else
//            setAddress(location.latitude, location.longitude)
    }

//    private fun setAddress(latitude: Double, longitude: Double) {
//        if (!Geocoder.isPresent())
//            return
//
//        // Create the `Geocoder` instance.
//        val geocoder = Geocoder(this, Locale.getDefault())
//
//        // After `Tiramisu Android OS`, it is needed to use a listener to avoid blocking the main
//        // thread waiting for results.
//        val geocodeListener = Geocoder.GeocodeListener { addresses ->
//            addresses.firstOrNull()?.toAddressString()?.let { address ->
//                binding.addressTextField?.editText?.setText(address)
//            }
//        }
//
//        // Return an array of Addresses that attempt to describe the area immediately surrounding
//        // the given latitude and longitude.
//        if (Build.VERSION.SDK_INT >= 33)
//            geocoder.getFromLocation(latitude, longitude, 1, geocodeListener)
//        else
//            geocoder.getFromLocation(latitude, longitude, 1)?.let {  addresses ->
//                addresses.firstOrNull()?.toAddressString()?.let { address ->
//                    binding.addressTextField?.editText?.setText(address)
//                }
//            }
//    }

    private fun Address.toAddressString() : String {
        val address = this

        // Create a `String` with multiple lines.
        val stringBuilder = StringBuilder()
        stringBuilder.apply {
            append(address.getAddressLine(0)).append("\n")
            append(address.postalCode).append(" ")
            append(address.locality).append("\n")
            append(address.countryName)
        }

        return stringBuilder.toString()
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        // Define the UI components listeners.
//        with (binding) {
//            viewModel.locationState.observe(viewLifecycleOwner) { location ->
//                latitudeTextField.editText?.setText(location.latitude.toString())
//                longitudeTextField.editText?.setText(location.longitude.toString())
//                timeTextField.editText?.setText(location.time.toDateString())
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
