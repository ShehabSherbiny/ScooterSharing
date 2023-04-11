package dk.itu.moapd.scootersharing.ahga.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.scootersharing.ahga.adapters.ScooterAdapter
import dk.itu.moapd.scootersharing.ahga.R
import dk.itu.moapd.scootersharing.ahga.activities.LoginActivity
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity.Companion.database
import dk.itu.moapd.scootersharing.ahga.dataClasses.Scooter
import dk.itu.moapd.scootersharing.ahga.helperClasses.SwipeToDeleteOrUpdateCallback
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentMainBinding
import dk.itu.moapd.scootersharing.ahga.helperClasses.DATABASE_URL

class MainFragment : Fragment() {

    companion object {
        private lateinit var adapter: ScooterAdapter
        fun isAdapterInit()= ::adapter.isInitialized
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = checkNotNull(_binding){
        "Cannot access binding because it is null. Is the view visible?"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainActivity.auth.currentUser?.let {
            val query = database.child("scooters")
            val options = FirebaseRecyclerOptions.Builder<Scooter>()
                .setQuery(query, Scooter::class.java)
                .setLifecycleOwner(this)
                .build()
            adapter = ScooterAdapter(options)
        }
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

                if (recyclerView.visibility == View.VISIBLE){
                    recyclerView.visibility = View.INVISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                }
            }

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            if (isAdapterInit()) {
                recyclerView.adapter = adapter
            }

            // IF THE USER IS NOT AUTH DON'T SHOW THE ADAPTER
            if (MainActivity.auth == null){
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
            }
        }
//        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteOrUpdateCallback(adapter))
  //      itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}