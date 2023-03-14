package dk.itu.moapd.scootersharing.ahga

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast.LENGTH_SHORT
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentUpdateRideBinding

class UpdateRideFragment : Fragment() {

    companion object {
        private val TAG = MainActivity :: class.qualifiedName
        lateinit var ridesDB: RidesDB
    }

    private var _binding: FragmentUpdateRideBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Cannot access binding because it is null. Is the view visible?"
    }

    private lateinit var scooter : Scooter

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        ridesDB = RidesDB.get(requireContext())
    }

    override fun onCreateView( // LAYOUT
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateRideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // LOGIC
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            val name = ridesDB.getCurrentScooter().name
            nameInput.setText(name)

            updateRideButton.setOnClickListener{
                if(nameInput.text.toString().isNotEmpty() && locationInput.text.toString().isNotEmpty()){

                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.app_name))
                        .setMessage(resources.getString(R.string.start_ride))
                        .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                            // Respond to neutral button press
                            binding.locationInput.text.clear()
                        }
                        .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                            // Respond to negative button press
                            binding.locationInput.text.clear()
                        }
                        .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                            // Respond to positive button press
                            val location = locationInput.text.toString().trim()

                            scooter = Scooter(name, location, System.currentTimeMillis())

                            nameInput.text.clear()
                            locationInput.text.clear()

                            ridesDB.updateCurrentScooter(location)

                            //SNACKBAR
                            val snack = Snackbar.make(it,scooter.toString(), LENGTH_SHORT)
                            snack.setAnchorView(updateRideButton.id)
                            snack.show()

                            findNavController().navigate(R.id.show_main_fragment)
                        }
                        .show()
                } else {
                    //SNACKBAR
                    val snack = Snackbar.make(it, "The field must not be empty",
                        BaseTransientBottomBar.LENGTH_SHORT
                    )
                    snack.setAnchorView(binding.updateRideButton.id)
                    snack.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showMessage(){
        Log.d(TAG, scooter.toString())
    }

}