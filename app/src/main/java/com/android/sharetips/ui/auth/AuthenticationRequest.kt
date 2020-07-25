package com.android.sharetips.ui.auth

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(@SerializedName("user") val userDto: UserDto)

data class UserDto(
    @SerializedName("username") val userName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegistrationResponse(
    @SerializedName("user") val user: User
)

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("username") val userName: String,
    @SerializedName("bio") val bio: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("token") val token: String
)

data class RegistrationErrorResponse(
    @SerializedName("errors") val registrationError: RegistrationError
)

data class RegistrationError(
    @SerializedName("email") val emailErrors: List<String>?,
    @SerializedName("username") val userNameErrors:  List<String>?,
    @SerializedName("password") val passwordErrors:  List<String>?
)
