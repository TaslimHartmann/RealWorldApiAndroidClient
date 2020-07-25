package com.android.sharetips.ui.auth

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {

    @POST("users")
    fun registration(@Body registrationRequest: RegistrationRequest): Call<RegistrationResponse>
}