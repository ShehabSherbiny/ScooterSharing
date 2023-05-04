package dk.itu.moapd.scootersharing.ahga

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
            .perform(clearText(), typeText("CPH001"))
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

    @Test
    fun populateScooterDatabase() {
        createScooter("CPH001", "ITU", 55.66040815474606, 12.591163331540711)
        createScooter("CPH002", "KU", 55.66292688383645, 12.588398542332436)
        createScooter("CPH003", "LUKSUS NETTO", 55.65654294862253, 12.589668417576462)
        createScooter("CPH004", "DR BYEN", 55.65854010445573, 12.590272476686216)
        createScooter("CPH005", "METRO", 55.655990635421794, 12.58913363905719)
        createScooter("CPH006", "KONCERTHUS", 55.658040421575464, 12.588934794317984)
    }

    private fun createScooter(name: String, location: String, lat: Double, long: Double) {
        onView(withId(R.id.register_new_scooter_button))
            .perform(click())

        onView(withId(R.id.name_input))
            .perform(clearText(), typeText(name))

        onView(withId(R.id.location_input))
            .perform(clearText(), typeText(location))

        onView(withId(R.id.latitude_input))
            .perform(clearText(), typeText(lat.toString()))

        onView(withId(R.id.longitude_input))
            .perform(clearText(), typeText(long.toString()))

        Espresso.closeSoftKeyboard()

        onView(withId(R.id.confirm_register_new_scooter_button))
            .perform(click())

        val acceptButton = onView(withText("Accept"))
        acceptButton.perform(click())
    }

}