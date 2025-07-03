package com.example.personalfinancemobile.app.data.model

data class BudgetingResponse(
    val success: Boolean,
    val message: String,
    val data: List<BudgetRequest>
)