package com.android.sharetips.ui.auth

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sharetips.ui.Failure
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val authenticationRepository = AuthenticationRepository()

    private val registrationState = MutableLiveData<RegistrationState>()
    fun registrationState(): LiveData<RegistrationState> = registrationState

    private val registrationSuccess = LiveEvent<RegistrationResponse>()
    fun registrationSuccess(): LiveData<RegistrationResponse> = registrationSuccess

    private val registrationError = LiveEvent<Failure>()
    fun registrationError(): LiveData<Failure> = registrationError

    fun validateUserName(userName: String, email: String, password: String) {
        when {
            userName.length >= MIN_USERNAME -> this.registrationState.value =
                RegistrationState.UserNameValid
            else -> this.registrationState.value = RegistrationState.UserNameTooShort
        }

        isFormValid(userName, email, password)
    }

    fun validateEmail(userName: String, email: String, password: String) {
        when {
            Patterns.EMAIL_ADDRESS.matcher(email).matches() -> this.registrationState.value =
                RegistrationState.EmailValid
            else -> this.registrationState.value = RegistrationState.EmailNotValid
        }

        isFormValid(userName, email, password)
    }

    fun validatePassword(userName: String, email: String, password: String) {
        when {
            password.length >= MIN_PASSWORD -> this.registrationState.value =
                RegistrationState.PasswordValid
            else -> this.registrationState.value = RegistrationState.PasswordTooShort
        }

        isFormValid(userName, email, password)
    }

    private fun isUserNameValid(userName: String): Boolean {
        return userName.length >= MIN_USERNAME
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= MIN_PASSWORD
    }

    private fun isFormValid(userName: String, email: String, password: String) {
        when {
            isUserNameValid(userName) && isEmailValid(email) && isPasswordValid(password) ->
                this.registrationState.value = RegistrationState.RegistrationValid
            else -> this.registrationState.value = RegistrationState.RegistrationNotValid
        }
    }

    fun registrationProcess(userName: String, email: String, password: String) {
        this.registrationState.value = RegistrationState.RegistrationStart

        viewModelScope.launch {
            val registrationResponse =
                authenticationRepository.registration(
                    RegistrationRequest(
                        UserDto(userName, email, password)
                    )
                )
            registrationResponse.either(::handleError, ::handleSuccess)
        }
    }

    private fun handleSuccess(registrationResponse: RegistrationResponse) {
        this.registrationState.value = RegistrationState.RegistrationFinished
        this.registrationSuccess.value = registrationResponse
    }

    private fun handleError(failure: Failure) {
        this.registrationState.value = RegistrationState.RegistrationFinished
        this.registrationError.value = failure
    }

    companion object {
        const val MIN_USERNAME = 6
        const val MIN_PASSWORD = 8
    }
}