package dk.itu.moapd.scootersharing.ahga.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dk.itu.moapd.scootersharing.ahga.R
import dk.itu.moapd.scootersharing.ahga.databinding.FragmentPayBinding
import dk.itu.moapd.scootersharing.ahga.helperClasses.Validation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PayFragment : Fragment() {

    companion object {
        private val TAG = PayFragment::class.qualifiedName
    }

    private var _binding: FragmentPayBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            var nameValidator = Validation(firstName)
            firstName.addTextChangedListener(nameValidator)
            var phoneValidator = Validation(phone)
            phone.addTextChangedListener(phoneValidator)
            var cardValidator = Validation(cardNumber)
            cardNumber.addTextChangedListener(cardValidator)

            googlePayButton.setOnClickListener {
                //check if the EditText have values or not
                if (!nameValidator.isValidName) {
                    Toast.makeText(requireContext(), "Enter valid first name", Toast.LENGTH_SHORT)
                        .show()
                }
                if (!phoneValidator.isValidPhone) {
                    Toast.makeText(requireContext(), "Enter valid phone", Toast.LENGTH_SHORT)
                        .show()
                }
                if (!cardValidator.isValidCardNumber) {
                    Toast.makeText(requireContext(), "Enter card number", Toast.LENGTH_SHORT)
                        .show()
                }

                if (firstName.text.toString().trim().isNotEmpty() &&
                    //lastName.text.toString().trim().isNotEmpty() &&
                    phone.text.toString().trim().isNotEmpty() &&
                    dateofbirth.text.toString().trim().isNotEmpty() &&
                    cardNumber.text.toString().trim().isNotEmpty() &&
                    expDate.text.toString().trim().isNotEmpty() &&
                    cvc.text.toString().trim().isNotEmpty()
                ) {
                    activity?.runOnUiThread {
                        lifecycleScope.launch {
                            progressBar.visibility = View.VISIBLE
                            delay(2000)
                            progressBar.visibility = View.GONE
                            findNavController().navigate(R.id.show_main_fragment)
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Please enter all fields ", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}