package com.example.personalfinancemobile.app.data.model
enum class Priode {
    Harian,
    Mingguan,
    Bulanan,
    Tahunan,
    Custom
}

data class Budget(
    val pemasukkan: Int,
    val priode: Priode,
    val categoryId: Int? = null
)

data class Category(
    val categoryId: Int,
    val name: String,
    val jumlah: Int
)


