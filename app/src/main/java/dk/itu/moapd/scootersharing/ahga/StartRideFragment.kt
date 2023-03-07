package dk.itu.moapd.scootersharing.ahga

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.ahga.databinding.ActivityStartRideBinding

class StartRideFragment : Fragment() {

    companion object {
        private val TAG = MainActivity :: class.qualifiedName
        private lateinit var ridesDB: RidesDB
    }

    private lateinit var binding: ActivityStartRideBinding
    private lateinit var scooter : Scooter

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        ridesDB = RidesDB.get(requireContext())
        binding = ActivityStartRideBinding.inflate(layoutInflater)

        //set event listener and implement logic
        binding.apply {


            startRideButton.setOnClickListener{
                if(startRideButton.text.isNotEmpty() && locationInput.text.isNotEmpty()){
                    val name = nameInput.text.toString().trim()
                    val location = locationInput.text.toString().trim()

                    scooter = Scooter(name, location)

                    val snack = Snackbar.make(it,scooter.toString(),10000)
                    snack.setAnchorView(startRideButton.id)
                    snack.show()

                    //reset textfields and update UI
                    nameInput.text.clear()
                    locationInput.text.clear()
                    val intent = Intent().apply{
                        putExtra("name", name)
                    }
                }
//                finish()
            }
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_ride, container, false)
    }

}