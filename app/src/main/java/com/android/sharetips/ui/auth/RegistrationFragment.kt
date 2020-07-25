package com.android.sharetips.ui.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.sharetips.R
import com.android.sharetips.ui.Failure
import kotlinx.android.synthetic.main.registration_fragment.*

class RegistrationFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "USER_TOKEN"

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.registration_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        viewModel.registrationState().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is RegistrationState.UserNameTooShort -> registrationUserNameLayout.error =
                    "Username terlalu pendek"
                is RegistrationState.UserNameValid -> registrationUserNameLayout.error = null
                is RegistrationState.EmailNotValid -> registrationEmailLayout.error =
                    "Email tidak valid"
                is RegistrationState.EmailValid -> registrationEmailLayout.error = null
                is RegistrationState.PasswordTooShort -> registrationPasswordLayout.error =
                    "Password terlalu pendek"
                is RegistrationState.PasswordValid -> registrationPasswordLayout.error = null
                is RegistrationState.RegistrationNotValid -> registrationSignInBtn.isEnabled = false
                is RegistrationState.RegistrationValid -> registrationSignInBtn.isEnabled = true
                is RegistrationState.RegistrationStart -> showProgress()
                is RegistrationState.RegistrationFinished -> hideProgress()
            }
        })

        viewModel.registrationError().observe(viewLifecycleOwner, Observer { fail ->
            when (fail) {
                is Failure.NetworkConnection -> Toast.makeText(
                    requireContext(), fail.message, Toast.LENGTH_SHORT
                ).show()
                is Failure.ErrorResponse -> Toast.makeText(
                    requireContext(), fail.message, Toast.LENGTH_LONG
                ).show()
            }
        })

        viewModel.registrationSuccess().observe(viewLifecycleOwner, Observer { response ->
            Log.d("Token: ", response.user.token)
            val editor = sharedPref.edit()
            editor.putString(PREF_NAME, response.user.token)
            editor.apply()
        })

        registrationUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.validateUserName(
                    registrationUserName.text.toString(),
                    registrationEmail.text.toString(),
                    registrationPassword.text.toString()
                )
            }
        })

        registrationEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.validateEmail(
                    registrationUserName.text.toString(),
                    registrationEmail.text.toString(),
                    registrationPassword.text.toString()
                )
            }
        })

        registrationPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.validatePassword(
                    registrationUserName.text.toString(),
                    registrationEmail.text.toString(),
                    registrationPassword.text.toString()
                )
            }
        })

        registrationSignInBtn.setOnClickListener {
            viewModel.registrationProcess(
                registrationUserName.text.toString(),
                registrationEmail.text.toString(),
                registrationPassword.text.toString()
            )
        }
        hideProgress()
        sharedPref = requireActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    }

    private fun hideProgress() {
        registrationProgress.visibility = View.GONE
    }

    private fun showProgress() {
        registrationProgress.visibility = View.VISIBLE
    }
}