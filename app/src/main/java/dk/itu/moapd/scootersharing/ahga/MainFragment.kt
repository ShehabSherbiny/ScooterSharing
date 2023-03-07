package dk.itu.moapd.scootersharing.ahga

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dk.itu.moapd.listview.ScooterArrayAdapter
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    companion object {
        private val TAG = MainActivity :: class.qualifiedName
        lateinit var ridesDB: RidesDB
        private lateinit var adapter: ScooterArrayAdapter
    }
    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "Cannot access"
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ridesDB = RidesDB.get(requireContext())
        binding.apply {
            startRideButton.setOnClickListener {
                findNavController().navigate(R.id.show_start_ride_fragment)
            }
            updateRideButton.setOnClickListener{
                findNavController().navigate(R.id.show_update_ride_fragment)
            }
            listRidesButton.setOnClickListener{
                // Define the list view adapter.
                listViewContainer.adapter = MainFragment.adapter
                //toggle visibility here
            }
        }

        val data = ArrayList<Scooter>()
        for (i in MainActivity.ridesDB.getRidesList())
            data.add(
                Scooter("Scooter Name ${i.name}",
                    "Location ${i.location}",
                    i.timestamp)
            )

        // Create the custom adapter to populate a list of dummy objects.
        MainFragment.adapter = ScooterArrayAdapter(this, R.layout.list_item, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}