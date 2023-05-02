package dk.itu.moapd.scootersharing.ahga.helperClasses

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import dk.itu.moapd.scootersharing.ahga.R
import java.util.regex.Pattern

/**
 * A class to validate the content from `EditText` components.
 */
class Validation(private val view: View) : TextWatcher {

    var isValidName = false
        private set

    var isValidPhone = false
        private set

    companion object {
        // This pattern works with Danish names.
        private val NAME_PATTERN = Pattern.compile(
            "^[A-Z]+[a-z]{2,}(?: [a-zA-Z]+)?(?: [a-zA-Z]+)?\$"
        )
        private val PHONE_PATTERN = Pattern.compile(
            "^[0-9()-]{5,10}\$"
        )

        fun isValidName(name: CharSequence): Boolean {
            return NAME_PATTERN.matcher(name).matches()
        }

        fun isValidPhone(phone: String): Boolean {
            return PHONE_PATTERN.matcher(phone).matches()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        when (view.id) {
            R.id.firstName ->
                isValidName = isValidName(s.toString())
            R.id.lastName ->
                isValidName = isValidName(s.toString())
            R.id.phone ->
                isValidPhone = isValidPhone(s.toString())
        }
    }

}