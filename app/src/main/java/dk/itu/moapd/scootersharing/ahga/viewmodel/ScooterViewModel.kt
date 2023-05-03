package dk.itu.moapd.scootersharing.ahga.viewmodel

import android.location.Location
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScooterViewModel: ViewModel() {

    // A list of all available fragments used by the main activity.
    // P.S.: These instances are created only once, when the app executes the `onCreate()` method for the first time.
    private val fragments = ArrayList<Fragment>()
    val currentScannedQr: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val scooterMatch: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }


    // This method will be executed when the main activity adds a new fragment into the user interface.
    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }

    // This method returns a list of all instances of fragments used by the main activity.
    fun getFragmentList() : List<Fragment> = fragments

    // The current selected fragment to show in the user interface.
    private val fragment = MutableLiveData<Fragment>()

    // A `LiveData` which publicly exposes any update in the current shown fragment.
    val fragmentState: LiveData<Fragment>
        get() = fragment

    // This method will be executed when the user selects a new fragment to show in the main activity.
    // It sets the text into the LiveData instance.
    fun setFragment(index: Int) {
        fragment.value = fragments.elementAt(index)
    }

}