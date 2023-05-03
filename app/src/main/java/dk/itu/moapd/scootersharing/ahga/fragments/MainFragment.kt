package dk.itu.moapd.scootersharing.ahga.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.StorageReference
import dk.itu.moapd.scootersharing.ahga.R
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity.Companion.currentScooter
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity.Companion.database
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity.Companion.onRide
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity.Companion.storage
import dk.itu.moapd.scootersharing.ahga.dataClasses.Rides
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentMainBinding
import dk.itu.moapd.scootersharing.ahga.services.ScooterService
import java.io.File
import java.util.*

class MainFragment : Fragment() {

    val service = Context.SENSOR_SERVICE
    private lateinit var sensorManager: SensorManager


    lateinit private var photoUri: Uri
    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { didTakePicture: Boolean ->
        val ref = currentScooter.name?.let { storage.reference.child(it + ".jpg") }
        if (ref != null) {
            uploadImageToBucket(photoUri, ref)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestUserPermissions()
    }

    override fun onCreateView( // LAYOUT
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // LOGIC
        super.onViewCreated(view, savedInstanceState)

        val service = Context.SENSOR_SERVICE
        sensorManager = requireActivity().getSystemService(service) as SensorManager

        binding.apply {
            if (onRide) {
                startRideButton.visibility = View.GONE
                endRideButton.visibility = View.VISIBLE
                currentScooterCard.visibility = View.VISIBLE

                currentScooterName.text = currentScooter.name
                currentScooterLocation.text = currentScooter.location

                val imageRef = storage.reference.child("${currentScooter.name}.jpg")

                imageRef.downloadUrl.addOnSuccessListener {
                    Glide.with(requireContext())
                        .load(it)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(binding.currentScooterImage)
                }

            }
            if (!onRide) {
                startRideButton.visibility = View.VISIBLE
                endRideButton.visibility = View.GONE
                currentScooterCard.visibility = View.GONE
            }

            startRideButton.setOnClickListener {
                if (onRide) {
                    val snack = Snackbar.make(
                        it,
                        "You must end your current ride before starting another", Toast.LENGTH_SHORT
                    )
                    snack.show()
                } else {

                    findNavController().navigate(R.id.show_start_ride_fragment)
                }
            }

            endRideButton.setOnClickListener {
                if (onRide) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("END RIDE")
                        .setMessage(
                            "Are you sure you want to end your current ride? \n\n" + currentScooter.name + "\nPrice: " + String.format(
                                "%.2f",
                                Math.random() * 100
                            ) + "dkk"
                        )
                        .setNegativeButton(R.string.decline) { dialog, which ->
                            // Respond to negative button press
                            return@setNegativeButton
                        }
                        .setPositiveButton(R.string.accept) { dialog, which ->
                            // Respond to positive button press
                            currentScooter.available = true
                            currentScooter.name?.let { it1 ->
                                database.child("scooters").child(it1).setValue(
                                    currentScooter
                                )
                            }

                            startRideButton.visibility = View.VISIBLE
                            endRideButton.visibility = View.GONE
                            currentScooterCard.visibility = View.GONE

                            val user = MainActivity.auth.currentUser
                            if (user != null) {
                                val uid = database.child("rides")
                                    .child("uid")
                                    .child(user.uid)
                                    .push()
                                    .key
                                uid?.let {
                                    if (!checkPermission()) {

                                        //ADD RIDE TO USERS RIDE HISTORY
                                        database.child("rides")
                                            .child(user.uid)
                                            .child(it)
                                            .setValue(
                                                Rides(
                                                    currentScooter,
                                                    startLatitude = currentScooter.latitude,
                                                    startLongitude = currentScooter.longitude,
                                                    endLatitude = ScooterService.currentLatitude,
                                                    endLongitude = ScooterService.currentLongitude,
                                                )
                                            )
                                        //UPDATE SCOOTER LOCATION
                                        currentScooter.latitude = ScooterService.currentLatitude
                                        currentScooter.longitude = ScooterService.currentLongitude
                                        MainActivity.auth.currentUser?.let {
                                            currentScooter.name?.let { scooterName ->
                                                MainActivity.database.child("scooters")
                                                    .child(scooterName).setValue(
                                                        currentScooter
                                                    )
                                            }
                                        }
                                    }
                                }
                            }
                            if (!checkPermission()) {
                                val photoName = "IMG_${Date()}.JPG"
                                val photoFile = File(
                                    requireContext().applicationContext.filesDir,
                                    photoName
                                )
                                photoUri = FileProvider.getUriForFile(
                                    requireContext(),
                                    "dk.itu.moapd.scootersharing.ahga.fileprovider",
                                    photoFile
                                )
                                takePhoto.launch(photoUri)
                            }

                            onRide = false
                            //SNACKBAR
                            val snack = Snackbar.make(
                                it,
                                "You have ended your current ride ", Toast.LENGTH_SHORT
                            )
                            snack.show()
                        }
                        .show()
                    //TODO: ADD PAYMENT
//                findNavController().navigate(R.id.show_payFragment)
                } else {
                    val snack = Snackbar.make(
                        it,
                        "You are not currently on a ride ", Toast.LENGTH_SHORT
                    )
                    snack.show()
                }
//                findNavController().navigate(R.id.show_payFragment)
            }
            listRidesButton.setOnClickListener {
                findNavController().navigate(R.id.show_ride_history_fragment)
            }
            mapButton.setOnClickListener {
                findNavController().navigate(R.id.show_maps_fragment)
            }
            cameraButton.setOnClickListener {
                requestUserPermissions()

                if (allPermissionsGranted()) {
                    val photoName = "IMG_${Date()}.JPG"
                    val photoFile = File(
                        requireContext().applicationContext.filesDir,
                        photoName
                    )
                    photoUri = FileProvider.getUriForFile(
                        requireContext(),
                        "dk.itu.moapd.scootersharing.ahga.fileprovider",
                        photoFile
                    )
                    takePhoto.launch(photoUri)
                }
            }
            qrButton.setOnClickListener {
                findNavController().navigate(R.id.show_qr_fragment)
            }
            paymentButton.setOnClickListener {
                findNavController().navigate(R.id.show_payFragment)
            }
            registerNewScooterButton.setOnClickListener {
                findNavController().navigate(R.id.show_register_new_scooter_fragment)
            }
            accelerometerButton.setOnClickListener {
                findNavController().navigate(R.id.show_linearAccelerationFragment)
            }
        }
    }

    private fun uploadImageToBucket(uri: Uri, image: StorageReference) {
        image.putFile(uri)
    }

    override fun onResume() {
        super.onResume()

        // Get an instance of the linear acceleration sensor and register the sensor listener.
        val linearAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        if (linearAcceleration != null)
            sensorManager.registerListener(
                linearAccelerationListener,
                linearAcceleration, SensorManager.SENSOR_DELAY_NORMAL
            )

        // Otherwise, inform to the users that there is no linear acceleration sensor in their
        // mobile devices.
        else {
            binding.apply {
                currentScooterAxisXValue.text = getString(R.string.unavailable)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        // When the Fragment is not visible, unregister the linear acceleration listener.
        sensorManager.unregisterListener(linearAccelerationListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // PERMISSIONS
    private fun requestUserPermissions() {
        val permissions: ArrayList<String> = ArrayList()
        permissions.add(Manifest.permission.CAMERA)
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        // Check which permissions is needed to ask to the user.
        val permissionsToRequest = permissionsToRequest(permissions)

        // Show the permissions dialogs to the user.
        if (permissionsToRequest.size > 0)
            requestPermissions(
                permissionsToRequest.toTypedArray(),
                MainActivity.REQUEST_CODE_PERMISSIONS
            )
    }

    private fun allPermissionsGranted() = MainActivity.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun permissionsToRequest(permissions: ArrayList<String>): ArrayList<String> {
        val result: ArrayList<String> = ArrayList()
        for (permission in permissions)
            if (activity?.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                result.add(permission)
        return result
    }

    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED


    // ACCELEROMETER
    private fun Float.normalize(): Int {
        val norm = java.lang.Float.min(
            java.lang.Float.max(this, -SensorManager.STANDARD_GRAVITY),
            SensorManager.STANDARD_GRAVITY
        )
        return ((norm + SensorManager.STANDARD_GRAVITY) /
                (2f * SensorManager.STANDARD_GRAVITY) * 100).toInt()
    }


    private val linearAccelerationListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            binding.apply {

                // VALUES BIGGER THAN 0
                if (event.values[0] > 0) {
                    currentScooterCircularProgressIndicatorX.progress = event.values[0].normalize()
                    currentScooterAxisXValue.text =
                        getString(R.string.speedometer, event.values[0] * 3.6)
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }
    }

}