/*
MIT License

Copyright (c) [2023] [Ahmed Bassam Galal & Alyson De Souza]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package dk.itu.moapd.scootersharing.ahga

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowCompat
import dk.itu.moapd.listview.ScooterArrayAdapter
import dk.itu.moapd.scootersharing.ahga.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity :: class.qualifiedName
        lateinit var ridesDB: RidesDB
        private lateinit var adapter: ScooterArrayAdapter
    }

    private lateinit var binding: ActivityMainBinding
    private var name : String? = null


    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        if(result.resultCode == Activity.RESULT_OK) {
            intent = result.data
            name = intent?.getStringExtra("name")
        }
    }

    /**
     * Method for creating a new activity
     * @param
     */

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState:Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        ridesDB = RidesDB.get(this)

        // Migrate from Kotlin synthetics to Jetpack view binding.
        // https://developer.android.com/topic/libraries/view-binding/migration
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set event listener and implement logic
        binding.apply {


            startRideButton.setOnClickListener{
                val intent = Intent(baseContext, StartRideActivity::class.java)
                startForResult.launch(intent)
            }
            updateRideButton.setOnClickListener{
                val intent = Intent(baseContext, UpdateRideActivity::class.java).apply {
                    putExtra("name", name)
                }
                startActivity(intent)
            }
            listRidesButton.setOnClickListener{
                // Define the list view adapter.
                listViewContainer.adapter = adapter
                //toggle visibility here
            }
        }

        // Create a dummy dataset with 100 elements.
        val data = ArrayList<Scooter>()
        for (i in ridesDB.getRidesList())
            data.add(
                Scooter("Scooter Name ${i.name}",
                    "Location ${i.location}",
                    i.timestamp)
            )

        // Create the custom adapter to populate a list of dummy objects.
        adapter = ScooterArrayAdapter(this, R.layout.list_item, data)

        // Inflate the user interface into the current activity.
        val view = binding.root
        setContentView(view)
    }
}