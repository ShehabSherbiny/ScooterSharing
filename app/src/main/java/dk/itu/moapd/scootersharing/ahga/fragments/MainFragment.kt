package dk.itu.moapd.scootersharing.ahga.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dk.itu.moapd.scootersharing.ahga.R
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = checkNotNull(_binding){
        "Cannot access binding because it is null. Is the view visible?"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView( // LAYOUT
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // LOGIC
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            startRideButton.setOnClickListener {
                findNavController().navigate(R.id.show_start_ride_fragment)
            }
            updateRideButton.setOnClickListener{
                findNavController().navigate(R.id.show_update_ride_fragment)
            }
            deleteRideButton.setOnClickListener{
                findNavController().navigate(R.id.show_delete_ride_fragment)
            }
            listRidesButton.setOnClickListener{
                findNavController().navigate(R.id.show_ride_history_fragment)
            }
            mapButton.setOnClickListener{
                findNavController().navigate(R.id.show_maps_fragment)
            }
            cameraButton.setOnClickListener{
                findNavController().navigate(R.id.show_camera_fragment)
            }
            registerNewScooterButton.setOnClickListener{
                findNavController().navigate(R.id.show_register_new_scooter_fragment)
            }

        }

//        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteOrUpdateCallback(adapter))
//        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}