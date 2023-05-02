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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity
import dk.itu.moapd.scootersharing.ahga.dataClasses.Rides
import dk.itu.moapd.scootersharing.ahga.databinding.RideHistoryItemRecyclerviewBinding

/**
 * A class to customize an adapter with a `ViewHolder` to populate a dummy dataset into a `ListView`.
 */

class HistoryAdapter(options: FirebaseRecyclerOptions<Rides>) :
    FirebaseRecyclerAdapter<Rides, HistoryAdapter.HistoryViewHolder>(options) {

    companion object {
        private val TAG = HistoryAdapter::class.qualifiedName
    }

    class HistoryViewHolder(private val binding: RideHistoryItemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rides: Rides) {
            binding.itemRideScooter.text = rides.scooter.name
            binding.itemRideEndLatitude.text = "Ending latitude" + rides.endLatitude.toString().substring(0, 5)
            binding.itemRideEndLongitude.text = "Ending longitude" + rides.endLongitude.toString().substring(0, 5)
            binding.itemRideStartLatitude.text =
                "Starting latitude" + rides.startLatitude.toString().substring(0, 5)
            binding.itemRideStartLongitude.text =
                "Ending latitude" + rides.startLongitude.toString().substring(0, 5)
            binding.itemRidePrice.text = "Price: " + rides.price.toString() + "dk"

            val imageRef = MainActivity.storage.reference.child("${rides.scooter.name}.jpg")

            imageRef.downloadUrl.addOnSuccessListener {
                Glide.with(itemView.context)
                    .load(it)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(binding.imageView)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        // Create a new view, which defines the UI of the list item
        val inflater = LayoutInflater.from(parent.context)
        val binding = RideHistoryItemRecyclerviewBinding.inflate(inflater, parent, false)

        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int, Rides: Rides) {
        holder.bind(Rides)
    }

}