/*
 * MIT License
 *
 * Copyright (c) 2023 Fabricio Batista Narcizo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package dk.itu.moapd.scootersharing.ahga.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.ahga.R
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity
import dk.itu.moapd.scootersharing.ahga.dataClasses.Scooter
import dk.itu.moapd.scootersharing.ahga.databinding.ScooterItemRecyclerviewBinding
import java.text.DateFormat

/**
 * A class to customize an adapter with a `ViewHolder` to populate a dummy dataset into a `ListView`.
 */
class ScooterAdapter(
    options: FirebaseRecyclerOptions<Scooter>,
    private val qrCodeScanner: ActivityResultLauncher<Void?>
) :
    FirebaseRecyclerAdapter<Scooter, ScooterAdapter.ViewHolder>(options) {

    companion object {
        private val TAG = ScooterAdapter::class.qualifiedName
    }

    class ViewHolder(private val binding: ScooterItemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(scooter: Scooter, qrCodeScanner: ActivityResultLauncher<Void?>) {
            binding.itemName.text = binding.root.context.getString(R.string.s_name, scooter.name)
            binding.itemLocation.text =
                binding.root.context.getString(R.string.s_location, scooter.location)
            binding.itemLatitude.text = scooter.latitude.toString().substring(0, 5)
            binding.itemLongitude.text = scooter.longitude.toString().substring(0, 5)
            binding.itemTime.text = DateFormat.getDateInstance().format(scooter.timestamp)

            if (scooter.available) {
                binding.Availability.text = "Available"
                binding.ScanQRCodeButton.visibility = View.VISIBLE
            } else {
                binding.Availability.text = "Not available"
            }
            binding.battery.text = "Battery level: " + scooter.batteryLevel.toString() + "%"

            val imageRef = MainActivity.storage.reference.child("${scooter.name}.jpg")

            imageRef.downloadUrl.addOnSuccessListener {
                Glide.with(itemView.context)
                    .load(it)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(binding.imageView)
            }
            // QR BUTTON
            binding.ScanQRCodeButton.setOnClickListener {
                qrCodeScanner.launch(null)

                // DIALOG
                if (scooter.available) {
                    MaterialAlertDialogBuilder(itemView.context)
                        .setTitle("START RIDE")
                        .setMessage("Are you sure you want to book the Scooter " + scooter.name + " ?")
                        .setNegativeButton(R.string.decline) { dialog, which ->
                            // Respond to negative button press
                            return@setNegativeButton
                        }
                        .setPositiveButton(R.string.accept) { dialog, which ->
                            // Respond to positive button press
                            scooter.available = false
                            MainActivity.currentScooter = scooter
                            MainActivity.onRide = true
                            scooter.name?.let { it1 ->
                                MainActivity.database.child("scooters").child(it1).setValue(scooter)
                            }
                            //SNACKBAR
                            val snack = Snackbar.make(
                                it,
                                "You have started a ride with scooter " + scooter.name,
                                Toast.LENGTH_SHORT
                            )
                            snack.show()
                        }
                        .show()
                } else {
                    //SNACKBAR
                    val snack = Snackbar.make(
                        it, "The chosen scooter is not available",
                        BaseTransientBottomBar.LENGTH_SHORT
                    )
                    snack.show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScooterItemRecyclerviewBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, scooter: Scooter) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Log.d(TAG, "Populate an item at position: $position")

        holder.bind(scooter, qrCodeScanner)
    }

}