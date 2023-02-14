package dk.itu.moapd.scootersharing.ahga

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity :: class.qualifiedName
    }
    private lateinit var scooterName : EditText
    private lateinit var location : EditText
    private lateinit var startRideButton: Button
    private val scooter : Scooter = Scooter ("", "")
    override fun onCreate(savedInstanceState:Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //set variables by getting information from "R"(look into what this is exactly)
        scooterName = findViewById(R.id.plain_text_input)
        location = findViewById(R.id.location_input)
        startRideButton = findViewById(R.id.start_ride_button)

        //set event listener and implement logic
        startRideButton.setOnClickListener{
            if(scooterName.text.isNotEmpty() && location.text.isNotEmpty()){
                val name = scooterName.text.toString().trim()
                val location2 = location.text.toString().trim()
                scooter.setName(name)
                scooter.setLocation(location2)

                //reset textfields and update UI
                scooterName.text.clear()
                location.text.clear()

                showMessage()
            }
        }

    }
    private fun showMessage(){
        Log.d(TAG,scooter.toString())
    }
}