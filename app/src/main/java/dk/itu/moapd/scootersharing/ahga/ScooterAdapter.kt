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
package dk.itu.moapd.listview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.scootersharing.ahga.R
import dk.itu.moapd.scootersharing.ahga.RidesDB
import dk.itu.moapd.scootersharing.ahga.Scooter
import dk.itu.moapd.scootersharing.ahga.databinding.ListItemBinding


/**
 * A class to customize an adapter with a `ViewHolder` to populate a dummy dataset into a `ListView`.
 */
class ScooterAdapter(private val ridesDB: RidesDB) :
    RecyclerView.Adapter<ScooterAdapter.ViewHolder>() {

    companion object {
        private val TAG = ScooterAdapter::class.qualifiedName
    }

    class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(scooter: Scooter){
            binding.itemName.text = binding.root.context.getString(R.string.s_name, scooter.name)
            binding.itemLocation.text = binding.root.context.getString(R.string.s_location, scooter.location)
            binding.itemTime.text = binding.root.context.getString(R.string.s_time, scooter.timestamp.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScooterAdapter.ViewHolder {

        // Create a new view, which defines the UI of the list item
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ScooterAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScooterAdapter.ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val scooter = ridesDB.getRidesList()[position]
        Log.d(ScooterAdapter.TAG, "Populate an item at position: $position")

        // Bind the view holder with the selected `DummyModel` data.
        holder.bind(scooter)
    }

    override fun getItemCount() = ridesDB.getRidesList().size

}