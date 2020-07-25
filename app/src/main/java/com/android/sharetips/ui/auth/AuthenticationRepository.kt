package com.android.sharetips.ui.auth

import com.android.sharetips.ui.Either
import com.android.sharetips.ui.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationRepository {

    private val authService: AuthenticationService = AuthenticationService()
    private val retrofit = AuthRequest.retrofitInstance

    suspend fun registration(registrationRequest: RegistrationRequest): Either<Failure, RegistrationResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authService.registration(registrationRequest).execute()
                if (response.isSuccessful) {
                    Either.Right(response.body() as RegistrationResponse)
                } else {

                    val errorResponse =
                        retrofit?.responseBodyConverter<RegistrationErrorResponse>(
                            RegistrationErrorResponse::class.java, arrayOfNulls(0)
                        )?.convert(response.errorBody()!!)
                    val error = errorResponse?.registrationError
                    var userNameError = ""
                    var emailError = ""
                    var passwordError = ""
                    if (error?.userNameErrors != null) {
                        userNameError = "Username ${error.userNameErrors[0]}\n"
                    }
                    if (error?.emailErrors != null) {
                        emailError = "Email ${error.emailErrors[0]}\n"
                    }
                    if (error?.passwordErrors != null) {
                        passwordError = "Password ${error.passwordErrors[0]}"
                    }
                    Either.Left(
                        Failure.ErrorResponse(userNameError + emailError + passwordError)
                    )
                }
            } catch (exception: Exception) {
                Either.Left(
                    Failure.NetworkConnection(
                        exception.message ?: "Terjadi kesalahan saat request"
                    )
                )
            }
        }
    }
}