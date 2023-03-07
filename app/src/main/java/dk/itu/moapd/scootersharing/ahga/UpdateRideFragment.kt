package dk.itu.moapd.scootersharing.ahga

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentUpdateRideBinding

class UpdateRideFragment : Fragment() {

    companion object {
        private val TAG = MainActivity :: class.qualifiedName
        lateinit var ridesDB: RidesDB
    }

    private lateinit var binding: FragmentUpdateRideBinding
    private lateinit var scooter : Scooter

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        UpdateRideFragment.ridesDB = RidesDB.get(requireContext())
        binding = FragmentUpdateRideBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        //set event listener and implement logic
        binding.apply {

//            val name = intent.getStringExtra("name")
//            nameInput.setText(name)

            updateRideButton.setOnClickListener{
                if(updateRideButton.text.isNotEmpty() && locationInput.text.isNotEmpty()){
                    val location = locationInput.text.toString().trim()

                    /* SnackBar:
                     scooter = Scooter(name, location, System.currentTimeMillis())
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
        Log.d(UpdateRideFragment.TAG, scooter.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_ride, container, false)
    }

}