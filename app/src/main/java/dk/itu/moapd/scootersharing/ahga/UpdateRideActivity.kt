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

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.WindowCompat
import dk.itu.moapd.scootersharing.ahga.databinding.ActivityUpdateRideBinding

class UpdateRideActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity :: class.qualifiedName
        lateinit var ridesDB: RidesDB
    }

    private lateinit var binding: ActivityUpdateRideBinding
    private lateinit var scooter : Scooter

    /**
     * Method for creating a new activity
     * @param
     */
    override fun onCreate(savedInstanceState:Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        ridesDB = RidesDB.get(this)
        binding = ActivityUpdateRideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set event listener and implement logic
        binding.apply {

            val name = intent.getStringExtra("name")
            nameInput.setText(name)

            updateRideButton.setOnClickListener{
                if(updateRideButton.text.isNotEmpty() && locationInput.text.isNotEmpty()){
                    val location = locationInput.text.toString().trim()
                   /* scooter = Scooter(name, location, System.currentTimeMillis())
                    val snack = Snackbar.make(it,scooter.toString(),1000)
                    snack.setAnchorView(updateRideButton.id)
                    snack.show()*/

                    //reset textfields and update UI
                    nameInput.text.clear()
                    locationInput.text.clear()

                }
            }
        }

    }
    private fun showMessage(){
        Log.d(TAG,scooter.toString())
    }
}