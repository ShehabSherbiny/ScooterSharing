package dk.itu.moapd.scootersharing.ahga

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentStartRideBinding

class StartRideFragment : Fragment() {

    companion object {
        private val TAG = MainActivity :: class.qualifiedName
        private lateinit var ridesDB: RidesDB
    }

    private lateinit var binding: FragmentStartRideBinding
    private lateinit var scooter : Scooter

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        ridesDB = RidesDB.get(requireContext())
        binding = FragmentStartRideBinding.inflate(layoutInflater)
//      setContentView(binding.root) // Only in the activity

    }

    // Opførsel i View
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set event listener and implement logic
        binding.apply {

            startRideButton.setOnClickListener{
                if(nameInput.text.toString().isNotEmpty() && locationInput.text.toString().isNotEmpty()){
                    val name = nameInput.text.toString().trim()
                    val location = locationInput.text.toString().trim()

                    val snack = Snackbar.make(it, ridesDB.getCurrentScooterInfo(),10000)
                    snack.setAnchorView(startRideButton.id)
                    snack.show()

                    ridesDB.addScooter(name, location)

                    //reset textfields and update UI
                    nameInput.text.clear()
                    locationInput.text.clear()

                    findNavController().navigate(R.id.show_main_fragment)
                }
            }
        }
    }

    //Laver View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_ride, container, false)
    }

}