package dk.itu.moapd.scootersharing.ahga.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import dk.itu.moapd.scootersharing.ahga.activities.LoginActivity
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity
import dk.itu.moapd.scootersharing.ahga.adapters.HistoryAdapter
import dk.itu.moapd.scootersharing.ahga.adapters.ScooterAdapter
import dk.itu.moapd.scootersharing.ahga.dataClasses.Rides
import dk.itu.moapd.scootersharing.ahga.dataClasses.Scooter
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentRideHistoryBinding
import dk.itu.moapd.scootersharing.ahga.databinding.RideHistoryItemRecyclerviewBinding

class RideHistoryFragment : Fragment() {

    companion object {
        private lateinit var adapter: HistoryAdapter
        fun isAdapterInit()= ::adapter.isInitialized
    }

    private var _binding: FragmentRideHistoryBinding? = null
    private val binding get() = checkNotNull(_binding){
        "Cannot access binding because it is null. Is the view visible?"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // IF THE USER IS NOT AUTH DON'T SHOW THE ADAPTER
        if (MainActivity.auth == null){
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        MainActivity.auth.currentUser?.let {
            val query = MainActivity.database.child("rides").child(it.uid)
            val options = FirebaseRecyclerOptions.Builder<Rides>()
                .setQuery(query, Rides::class.java)
                .setLifecycleOwner(this)
                .build()
            adapter = HistoryAdapter(options)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentRideHistoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            if (isAdapterInit()) {
                recyclerView.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}