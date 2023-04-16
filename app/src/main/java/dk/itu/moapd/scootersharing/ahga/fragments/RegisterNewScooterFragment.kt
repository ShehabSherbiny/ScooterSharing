package dk.itu.moapd.scootersharing.ahga.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity
import dk.itu.moapd.scootersharing.ahga.R
import dk.itu.moapd.scootersharing.ahga.dataClasses.RidesDB
import dk.itu.moapd.scootersharing.ahga.dataClasses.Scooter
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentRegisterNewScooterBinding

class RegisterNewScooterFragment : Fragment() {

    companion object {
        private val TAG = MainActivity :: class.qualifiedName
        private lateinit var ridesDB: RidesDB
    }

    private var _binding: FragmentRegisterNewScooterBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Cannot access binding because it is null. Is the view visible?"
    }

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        ridesDB = RidesDB.get(requireContext())
    }

    override fun onCreateView( // LAYOUT
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterNewScooterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // LOGIC
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            binding.registerNewScooterButton.setOnClickListener{
                if(binding.nameInput.text.toString().isNotEmpty() && binding.locationInput.text.toString().isNotEmpty()){

                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.app_name))
                        .setMessage(resources.getString(R.string.start_ride) + ": " + binding.nameInput.text.toString() + " from " + binding.locationInput.text.toString())
                        .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                            // Respond to neutral button press
                            binding.nameInput.text.clear()
                            binding.locationInput.text.clear()
                        }
                        .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                            // Respond to negative button press
                            binding.nameInput.text.clear()
                            binding.locationInput.text.clear()
                        }
                        .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                            // Respond to positive button press
                            val name = binding.nameInput.text.toString().trim()
                            val location = binding.locationInput.text.toString().trim()
                            val scooter =  Scooter(name, location)
                            MainActivity.auth.currentUser?.let{
                                val id = MainActivity.database.child("scooters").
                                child(name).push()
                                id?.let {
                                    MainActivity.database.child("scooters").child(name).setValue(scooter)
                                }
                            }

                            //ridesDB.addScooter(name, location)

                            binding.nameInput.text.clear()
                            binding.locationInput.text.clear()

                            //SNACKBAR
                            val snack = Snackbar.make(it, ridesDB.getCurrentScooterInfo(),LENGTH_SHORT)
                            snack.setAnchorView(binding.registerNewScooterButton.id)
                            snack.show()

                            findNavController().navigate(R.id.show_main_fragment)
                        }
                        .show()
                } else {
                    //SNACKBAR
                    val snack = Snackbar.make(it, "The field must not be empty",LENGTH_SHORT)
                    snack.setAnchorView(binding.registerNewScooterButton.id)
                    snack.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}