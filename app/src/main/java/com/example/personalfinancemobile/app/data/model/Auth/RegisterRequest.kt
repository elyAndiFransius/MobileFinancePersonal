package com.example.personalfinancemobile.app.data.model.Auth

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val confirm_password: String
)
