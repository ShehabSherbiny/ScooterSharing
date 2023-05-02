package dk.itu.moapd.scootersharing.ahga

import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Lifecycle.State
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dk.itu.moapd.scootersharing.ahga.activities.MainActivity
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("dk.itu.moapd.scootersharing.ahga", appContext.packageName)
    }

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivityTest_createMainActivity() {
        val scenario = activityScenarioRule.scenario
        scenario.moveToState(State.RESUMED)
    }

    @Test
    fun mainActivityTest_addNewScooterToDatabase_with_RegisterNewScooterFragment() {

        // Click on Register New Scooter Button.
        onView(withId(R.id.register_new_scooter_button))
            .perform(click())

        // All EditTexts are empty.
        onView(withId(R.id.name_input))
            .check(matches(withText("")))
        onView(withId(R.id.location_input))
            .check(matches(withText("")))
        onView(withId(R.id.latitude_input))
            .check(matches(withText("")))
        onView(withId(R.id.longitude_input))
            .check(matches(withText("")))

        // Insert data into the EditTexts.
        onView(withId(R.id.name_input))
            .perform(clearText(), typeText("CPH002"))
        // Hide Keyboard
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.location_input))
            .perform(clearText(), typeText("ITU"))

        // Hide Keyboard
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.latitude_input))
            .perform(clearText(), typeText("55.6596"))

        // Hide Keyboard
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.longitude_input))
            .perform(clearText(), typeText("12.5910"))

        // Hide Keyboard
        Espresso.closeSoftKeyboard()

        // Create Scooter Button.
        onView(withId(R.id.confirm_register_new_scooter_button))
            .perform(click())

        // Accept Card
        val acceptButton = onView(withText("Accept"))
        acceptButton.perform(click())

        // Wait for 2 seconds
        Thread.sleep(2000)
    }

}