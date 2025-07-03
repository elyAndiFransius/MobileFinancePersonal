package com.example.personalfinancemobile.app.data.model

import android.os.Parcelable
import com.example.personalfinancemobile.app.ui.adapter.Category
import kotlinx.parcelize.Parcelize


data class BudgetRequest(
    val pemasukkan: Int,
    val priode: String,
    val categories: List<CategoryRequest>
)
@Parcelize
data class CategoryRequest(
    val name: String,
    val jumlah: Int
) : Parcelable
