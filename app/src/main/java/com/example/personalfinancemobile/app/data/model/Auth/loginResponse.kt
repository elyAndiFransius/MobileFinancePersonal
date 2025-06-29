package com.example.personalfinancemobile.app.data.model.Auth

import com.example.personalfinancemobile.app.data.model.User

data class loginResponse(
    val massage: String,
    val access_token: String,
    val token_type: String,
    val user: UserResponse
)
