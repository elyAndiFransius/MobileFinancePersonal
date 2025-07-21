package com.example.personalfinancemobile.app.data.model

data class OTPResponse(
    val success: Boolean,
    val message: String
)

data class SentOTPResponse(
    val success: Boolean,
    val message: String
)