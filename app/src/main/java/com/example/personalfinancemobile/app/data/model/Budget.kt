package com.example.personalfinancemobile.app.data.model


enum class Priode {
    HARIAN,
    MINGGUAN,
    BULANAN,
    TAHUNAN
}

data class Budget(
    val budgetId: Int,
    val pemasukkan: Int,
    val priode: Priode,
    val categoryId: Int
)

data class Category(
    val categoryId: Int,
    val name: String,
    val jumlah: Int
)


