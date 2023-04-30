package dk.itu.moapd.scootersharing.ahga.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dk.itu.moapd.scootersharing.ahga.R

class QrFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val options = BarcodeScannerOptions.Builder()
//            .setBarcodeFormats(
//                Barcode.FORMAT_QR_CODE,
//                Barcode.FORMAT_AZTEC
//            )
//            .build()
//        val scanner = BarcodeScanning.getClient(options)

        // Or, to specify the formats to recognize:
        // val scanner = BarcodeScanning.getClient(options)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr, container, false)
    }

}