package com.android.sharetips.ui

sealed class Failure {
    data class NetworkConnection(val message: String) : Failure()
    data class ErrorResponse(val message: String) : Failure()
}