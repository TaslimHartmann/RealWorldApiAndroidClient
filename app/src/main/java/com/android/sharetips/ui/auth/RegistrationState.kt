package com.android.sharetips.ui.auth

sealed class RegistrationState {
    object UserNameTooShort: RegistrationState()
    object UserNameValid: RegistrationState()
    object EmailNotValid: RegistrationState()
    object EmailValid: RegistrationState()
    object PasswordTooShort: RegistrationState()
    object PasswordValid: RegistrationState()
    object RegistrationNotValid: RegistrationState()
    object RegistrationValid: RegistrationState()
    object RegistrationStart: RegistrationState()
    object RegistrationFinished: RegistrationState()
}