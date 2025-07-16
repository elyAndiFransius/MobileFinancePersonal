package com.example.personalfinancemobile.app.data.model


import java.io.Serializable

data class TransactionModel(
    val id: Int,
    val categoryField: String,
    val jenis: String,
    val date: String,
    val descripsi: String,
    val jumlah: Int
) : Serializable