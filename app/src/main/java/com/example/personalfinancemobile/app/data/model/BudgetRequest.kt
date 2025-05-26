package com.example.personalfinancemobile.app.data.model

import com.example.personalfinancemobile.app.ui.adapter.Category

data class BudgetRequest(
    val pemasukkan: Int,
    val priode: String,
    val category: List<CategoryRequest>
)
data class CategoryRequest(
    val name: String,
    val jumlah: Int
)
