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
    var isValidCardNumber = false
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


        //source: https://gist.github.com/EmmanuelGuther/1ebe1bc2b00f7a540a93c3f7d62bf3e3
        fun isValidCard(cardNumber: String): Boolean {
            var s1 = 0
            var s2 = 0
            val reverse = StringBuffer(cardNumber).reverse().toString()
            for (i in reverse.indices) {
                val digit = Character.digit(reverse[i], 10)
                when {
                    i % 2 == 0 -> s1 += digit
                    else -> {
                        s2 += 2 * digit
                        when {
                            digit >= 5 -> s2 -= 9
                        }
                    }
                }
            }
            return (s1 + s2) % 10 == 0
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
            R.id.cardNumber ->
                isValidCardNumber = isValidCard(s.toString())
        }
    }

}