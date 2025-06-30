package com.example.personalfinancemobile.app.data.model

data class TransactionResponse(
    val ssuccess: Boolean,
    val message: String,
    val data: List<TransactionModel>
)
