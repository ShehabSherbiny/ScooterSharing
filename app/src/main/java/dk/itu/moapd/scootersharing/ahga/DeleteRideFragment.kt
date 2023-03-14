package dk.itu.moapd.scootersharing.ahga

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentDeleteRideBinding
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentStartRideBinding

class DeleteRideFragment : Fragment() {

    companion object {
        private val TAG = MainActivity :: class.qualifiedName
        private lateinit var ridesDB: RidesDB
    }

    private var _binding: FragmentDeleteRideBinding? = null
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
        _binding = FragmentDeleteRideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // LOGIC
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            binding.deleteRideButton.setOnClickListener{
                if(binding.nameInput.text.toString().isNotEmpty()){

                    val name = binding.nameInput.text.toString().trim()

                    if (ridesDB.containsScooter(name)){

                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(resources.getString(R.string.app_name))
                            .setMessage(resources.getString(R.string.delete_ride) + ": " + binding.nameInput.text.toString())
                            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                                // Respond to neutral button press
                                binding.nameInput.text.clear()
                            }
                            .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                                // Respond to negative button press
                                binding.nameInput.text.clear()
                            }
                            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                                // Respond to positive button press
                                ridesDB.deleteScooter(name)

                                //SNACKBAR
                                val snack = Snackbar.make(it, "Ride deleted",LENGTH_SHORT)
                                snack.setAnchorView(binding.deleteRideButton.id)
                                snack.show()

                                binding.nameInput.text.clear()

                                findNavController().navigate(R.id.show_main_fragment)
                            }
                            .show()
                    } else {
                        //SNACKBAR
                        val snack = Snackbar.make(it, "Ride doesn't exist\nCheck your rides at the 'List Rides'",LENGTH_SHORT)
                        snack.setAnchorView(binding.deleteRideButton)
                        snack.show()
                    }
                } else {
                    //SNACKBAR
                    val snack = Snackbar.make(it, "The field must not be empty",LENGTH_SHORT)
                    snack.setAnchorView(binding.deleteRideButton.id)
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