package dk.itu.moapd.scootersharing.ahga.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import dk.itu.moapd.scootersharing.ahga.activities.LoginActivity
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity
import dk.itu.moapd.scootersharing.ahga.adapters.ScooterAdapter
import dk.itu.moapd.scootersharing.ahga.dataClasses.Scooter
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentRideHistoryBinding

class StartRideFragment : Fragment() {

    companion object {
        private lateinit var adapter: ScooterAdapter
        fun isAdapterInit() = ::adapter.isInitialized
    }

    private var _binding: FragmentRideHistoryBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
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
                if (barcodeScanned == "CPHO01") { //Find in a Scooter List ?
                    requireContext().run {
                        //TODO: START RIDE
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // IF THE USER IS NOT AUTH DON'T SHOW THE ADAPTER
        if (MainActivity.auth == null) {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        MainActivity.auth.currentUser?.let {
            val query = MainActivity.database.child("scooters")
            val options = FirebaseRecyclerOptions.Builder<Scooter>()
                .setQuery(query, Scooter::class.java)
                .setLifecycleOwner(this)
                .build()
            adapter = ScooterAdapter(options, qrCodeScanner)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRideHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            if (isAdapterInit()) {
                recyclerView.adapter = adapter
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}