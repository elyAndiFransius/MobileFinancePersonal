package com.example.personalfinancemobile.app.data.model

data class DepositResponse(
    val success: Boolean,
    val message: String,
    val data: List<ServerDeposit>
)
