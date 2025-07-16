package com.example.personalfinancemobile.app.data.model.Auth

data class GrafikCategori(
    val kategori: String,
    val jumlah: Int,
    val pemasukkan: Int
)

data class GrafikTarget(
    val gol: String,
    val targetAmount: Int,
    val currentAmount: Int
)
