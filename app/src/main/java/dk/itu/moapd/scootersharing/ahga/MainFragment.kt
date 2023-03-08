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
        lateinit var ridesDB: RidesDB
        private lateinit var adapter: ScooterArrayAdapter
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = checkNotNull(_binding){
        "Cannot access binding because it is null. Is the view visible?"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ridesDB = RidesDB.get(requireContext())

        adapter = ScooterArrayAdapter(requireContext(), R.layout.list_item, ridesDB.getRidesList())
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
            listRidesButton.setOnClickListener{
                listViewContainer.adapter = adapter
                //TODO: toggle visibility here
            }
        }

        val data = ArrayList<Scooter>()
        for (i in MainActivity.ridesDB.getRidesList())
            data.add(
                Scooter("Scooter Name ${i.name}",
                    "Location ${i.location}",
                    i.timestamp)
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}