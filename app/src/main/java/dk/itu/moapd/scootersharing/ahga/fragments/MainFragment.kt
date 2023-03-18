package dk.itu.moapd.scootersharing.ahga.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import dk.itu.moapd.scootersharing.ahga.adapters.ScooterAdapter
import dk.itu.moapd.scootersharing.ahga.R
import dk.itu.moapd.scootersharing.ahga.dataClasses.RidesDB
import dk.itu.moapd.scootersharing.ahga.helperClasses.SwipeToDeleteOrUpdateCallback
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    companion object {
        lateinit var ridesDB: RidesDB
        private lateinit var adapter: ScooterAdapter
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = checkNotNull(_binding){
        "Cannot access binding because it is null. Is the view visible?"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ridesDB = RidesDB.get(requireContext())

        adapter = ScooterAdapter(ridesDB)

        _binding = FragmentMainBinding.inflate(layoutInflater)

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

            recyclerView.layoutManager = LinearLayoutManager(requireContext())

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

                recyclerView.adapter = adapter
                if (recyclerView.visibility == View.VISIBLE){
                    recyclerView.visibility = View.INVISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteOrUpdateCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}