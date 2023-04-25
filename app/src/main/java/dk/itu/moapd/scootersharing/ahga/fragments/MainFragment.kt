package dk.itu.moapd.scootersharing.ahga.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.storage.StorageReference
import dk.itu.moapd.scootersharing.ahga.R
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity.Companion.database
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity.Companion.storage
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentMainBinding
import java.io.File
import java.util.*


class MainFragment : Fragment() {

    lateinit private var photoUri: Uri
    private var _binding: FragmentMainBinding? = null
    private val binding get() = checkNotNull(_binding){
        "Cannot access binding because it is null. Is the view visible?"
    }

    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {  didTakePicture: Boolean ->
        val ref= storage.reference.child("images/lol.jpg")
        uploadImageToBucket(photoUri, ref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView( // LAYOUT
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // LOGIC
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            startRideButton.setOnClickListener {
                findNavController().navigate(R.id.show_start_ride_fragment)
            }
            updateRideButton.setOnClickListener{
                findNavController().navigate(R.id.show_update_ride_fragment)
            }
            deleteRideButton.setOnClickListener{
                findNavController().navigate(R.id.show_delete_ride_fragment)
            }
            listRidesButton.setOnClickListener{
                findNavController().navigate(R.id.show_ride_history_fragment)
            }
            mapButton.setOnClickListener{
                findNavController().navigate(R.id.show_maps_fragment)
            }
            cameraButton.setOnClickListener{
                requestUserPermissions()

                if(allPermissionsGranted()) {
                    val photoName = "IMG_${Date()}.JPG"
                    val photoFile = File(requireContext().applicationContext.filesDir,
                        photoName)
                    photoUri = FileProvider.getUriForFile(
                        requireContext(),
                        "dk.itu.moapd.scootersharing.ahga.fileprovider",
                        photoFile
                    )

                    takePhoto.launch(photoUri)


                }



            }
            qrButton.setOnClickListener{
                findNavController().navigate(R.id.show_qr_fragment)
            }
            registerNewScooterButton.setOnClickListener{
                findNavController().navigate(R.id.show_register_new_scooter_fragment)
            }

        }

//        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteOrUpdateCallback(adapter))
//        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }
    private fun imageResult(result: ActivityResult){
       // if(resu)
    }

    private fun uploadImageToBucket(uri: Uri, image: StorageReference) {
        image.putFile(uri)
    }

   /* private fun saveImageInDatabase(url: String, path: String) {
        val timestamp = System.currentTimeMillis()
        val image =  Image(url, path, timestamp)

        // In the case of authenticated user, create a new unique key for the object in the
        // database.
        MainActivity.auth.currentUser?.let { user ->
            val uid = storage.reference.child("images")
                .child(user.uid)


            // Insert the object in the database.
            uid?.let {
                database.child("images")
                    .child(user.uid)
                    .setValue(image)
            }
        }
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun requestUserPermissions() {
        val permissions: ArrayList<String> = ArrayList()
        permissions.add(Manifest.permission.CAMERA)

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
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }


    private fun permissionsToRequest(permissions: ArrayList<String>): ArrayList<String> {
        val result: ArrayList<String> = ArrayList()
        for (permission in permissions)
            if (activity?.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                result.add(permission)
        return result
    }

}