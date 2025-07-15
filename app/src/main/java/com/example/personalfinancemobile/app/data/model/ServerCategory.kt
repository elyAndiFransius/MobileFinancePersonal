package com.example.personalfinancemobile.app.data.model

import java.io.Serializable

data class ServerCategory(
    val id: Int,
    val name: String,
    val jumlah: Int
) : Serializable
