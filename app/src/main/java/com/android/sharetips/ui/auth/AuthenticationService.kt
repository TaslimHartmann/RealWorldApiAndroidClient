package com.android.sharetips.ui.auth

import retrofit2.Call

class AuthenticationService : AuthenticationApi {

    private val authenticationApi: AuthenticationApi = AuthRequest.retrofitInstance!!.create(AuthenticationApi::class.java)

    override fun registration(registrationRequest: RegistrationRequest): Call<RegistrationResponse> {
        return authenticationApi.registration(registrationRequest)
    }
}