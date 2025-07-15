package com.example.personalfinancemobile.app.data.model

data class CategoryResponse(
    val success: Boolean,
    val message: String,
    val data: List<ServerCategory>
)
