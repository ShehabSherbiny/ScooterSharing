package dk.itu.moapd.scootersharing.ahga.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentQrBinding

class QrFragment : Fragment() {

    private val TAG = MainActivity::class.qualifiedName

    private var _binding: FragmentQrBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private var scooter: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC
            )
            .build()

        binding.apply {
            scanButton.setOnClickListener {
                qrCodeScanner.launch(null)
            }
        }
    }

    val qrCodeScanner = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) {
        val client = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        )

        if (it != null) {
            client.process(it, 0).addOnSuccessListener { barcodes ->
                val barcodeScanned = barcodes.firstOrNull()?.rawValue ?: "Unknown"
                if (barcodeScanned == scooter) { //Find in a Scooter List ?
                    requireContext().run {
                        //START RIDE
                        //findNavController().navigate(R.id.show_start_ride_fragment)

                        // SNACKBAR
                        view?.let { snackView ->
                            Snackbar.make(
                                snackView, "SCOOTER FOUND: " + barcodeScanned, Toast.LENGTH_SHORT
                            )
                        }?.show()
                    }
                } else {

                    // SNACKBAR
                    view?.let { snackView ->
                        Snackbar.make(
                            snackView,
                            "BARCODE DON'T MATCH SCOOTER: " + barcodeScanned,
                            Toast.LENGTH_SHORT
                        )
                    }?.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentQrBinding.inflate(inflater, container, false)
        return binding.root
    }

}