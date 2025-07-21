package com.example.personalfinancemobile.app.data.model

import com.example.personalfinancemobile.app.data.model.Auth.UserResponse
import java.util.Date

data class User (
    val name: String,
    val email: String,
    val password: String,
    val confirm_password: String
)

data class UserResponseObject(
    val success: Boolean,
    val user: showUser
)

data class showUser(
    val id: Int,
    val name: String,
    val email: String,
    val email_verified_at : String,
    val created_at: Date,
    val updated_at: Date,
    val token_code : String,
    val verification: Int
)
data class Validasi (
    val email: String,
    val code_verification: String
)

data class sentOTP (
    val email: String
)

data class resetPassword (
    val email: String,
    val password: String,
)

data class resetPasswordOpt (
    val email: String,
    val code_verification: String,
)




