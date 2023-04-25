package dk.itu.moapd.scootersharing.ahga.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.findFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentCameraBinding
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Core
import org.opencv.core.CvType.CV_8UC4
import org.opencv.core.Mat

class CameraFragment : Fragment(), CameraBridgeViewBase.CvCameraViewListener2 {

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Cannot access binding because it is null. Is the view visible?"
    }

    private var cameraCharacteristics = CameraCharacteristics.LENS_FACING_BACK
    private lateinit var loaderCallback: BaseLoaderCallback
    private lateinit var imageMat: Mat
    private var currentMethodId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = FragmentCameraBinding.inflate(layoutInflater)

        // The current selected camera.
        cameraCharacteristics = CameraCharacteristics.LENS_FACING_BACK

        // The current selected method.
       /* currentMethodId =
            viewModel.methodId.value ?: 0
        viewModel.methodId.observe(this) {
            currentMethodId = it
        }*/

        // Request camera permissions.
        if (allPermissionsGranted())
            startCamera()
        else
            requestUserPermissions()

        // Define the UI behavior.
        binding.apply {

            // Listener for button used to switch cameras.
//           cameraSwitchButton.setOnClickListener {
//                viewModel.onCameraCharacteristicsChanged(
//                    if (CameraCharacteristics.LENS_FACING_FRONT == cameraCharacteristics)
//                        CameraCharacteristics.LENS_FACING_BACK
//                    else
//                        CameraCharacteristics.LENS_FACING_FRONT
//                )
//
//                // Re-start use cases to update selected camera.
//                cameraView.disableView()
//                cameraView.setCameraIndex(cameraCharacteristics)
//                cameraView.enableView()
//            }

            // Listener for button used to change the image analysis method.
           /* imageAnalysisButton.setOnClickListener {
                var methodId = currentMethodId + 1
                methodId %= 4
                viewModel.onMethodChanged(methodId)
            }*/

            cameraView.setCameraIndex(cameraCharacteristics)
            cameraView.enableView()

            cameraCaptureButton.setOnClickListener{
                startCamera()
            }

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun startCamera() {

        // Setup the OpenCV camera view.
        binding.cameraView.apply {
            visibility = SurfaceView.VISIBLE
            setCameraIndex(cameraCharacteristics)
            setCameraPermissionGranted()
            setCvCameraViewListener(this@CameraFragment)
        }

        // Initialize the callback from OpenCV Manager to handle the OpenCV library.
        loaderCallback = object : BaseLoaderCallback(requireContext()) {
            override fun onManagerConnected(status: Int) {
                when (status) {
                    SUCCESS -> binding.cameraView.enableView()
                    else -> super.onManagerConnected(status)
                }
            }
        }
    }
    override fun onCameraViewStarted(width: Int, height: Int) {
        // Create the OpenCV Mat structure to represent images in the library.
        imageMat = Mat(height, width, CV_8UC4)
    }
    override fun onCameraViewStopped() {
        imageMat.release()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {

        // Get the current frame and copy it to the OpenCV Mat structure.
        val image = inputFrame?.rgba()
        imageMat = image!!

        if (cameraCharacteristics == CameraCharacteristics.LENS_FACING_BACK)
            Core.flip(image, image, 1)

        return image
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Check if the user has accepted the permissions to access the camera.
        if (requestCode == REQUEST_CODE_PERMISSIONS)
            if (allPermissionsGranted())
                startCamera()

            // If permissions are not granted, present a toast to notify the user that the
            // permissions were not granted.
            else {
                snackBar("Permissions not granted by the user.")
            }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun snackBar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT) {
//        Snackbar
//            .make(findViewById(R.id.content_main), text, duration)
//            .show()

//        //SNACKBAR
        val snack = Snackbar.make(binding.cameraView, text, duration)
        snack.show()
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
                REQUEST_CODE_PERMISSIONS
            )

    }

    private fun permissionsToRequest(permissions: ArrayList<String>): ArrayList<String> {
        val result: ArrayList<String> = ArrayList()
        for (permission in permissions)
            if (activity?.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                result.add(permission)
        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}