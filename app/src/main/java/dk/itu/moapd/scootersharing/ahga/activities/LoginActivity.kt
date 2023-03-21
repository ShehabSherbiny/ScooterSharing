package dk.itu.moapd.scootersharing.ahga.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.ahga.R


/**
 * An activity class with methods to manage the login activity of Firebase Authentication
 * application.
 */
class LoginActivity : AppCompatActivity() {

    /**
     * This object launches a new activity and receives back some result data.
     */
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        onSignInResult(result)
    }

    /**
     * Called when the activity is starting. This is where most initialization should go: calling
     * `setContentView(int)` to inflate the activity's UI, using `findViewById()` to
     * programmatically interact with widgets in the UI, calling
     * `managedQuery(android.net.Uri, String[], String, String[], String)` to retrieve cursors for
     * data being displayed, etc.
     *
     * You can call `finish()` from within this function, in which case `onDestroy()` will be
     * immediately called after `onCreate()` without any of the rest of the activity lifecycle
     * (`onStart()`, `onResume()`, onPause()`, etc) executing.
     *
     * <em>Derived classes must call through to the super class's implementation of this method. If
     * they do not, an exception will be thrown.</em>
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut
     * down then this Bundle contains the data it most recently supplied in `onSaveInstanceState()`.
     * <b><i>Note: Otherwise it is null.</i></b>
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createSignInIntent()
    }

    /**
     * This method uses FirebaseUI to create a login activity with three sign-in/sign-up options,
     * namely: (1) by e-mail, (2) by phone number, and (3) by Google account. The user interface is
     * pre-defined and it uses the same theme (i.e., Material Design) to define the login activity
     * style.
     */
    private fun createSignInIntent() {

        // Choose authentication providers.
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent.
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.firebase_icon)
            .setTheme(R.style.Theme_ScooterSharing)
            .build()
        signInLauncher.launch(signInIntent)
    }

    /**
     * When the second activity finishes (i.e., the pre-define login activity), it returns a result
     * to this activity. If the user sign-in the application correctly, we redirect the user to the
     * main activity of this application.
     *
     * @param result A result describing that the caller can launch authentication flow with a
     *      `Intent` and is guaranteed to receive a `FirebaseAuthUIAuthenticationResult` as
     *      result.
     */
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // Sign in success, update UI with the signed-in user's information.
            snackBar("User logged in the app.")
            startMainActivity()
        } else
        // If sign in fails, display a message to the user.
            snackBar("Authentication failed.")
    }

    /**
     * In the case of successfully login, it opens the main activity and starts the Firebase
     * Authentication application.
     */
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Make a standard snack-bar that just contains text.
     */
    private fun snackBar(text: CharSequence,
                         duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar
            .make(findViewById(android.R.id.content), text, duration)
            .show()
    }

}
