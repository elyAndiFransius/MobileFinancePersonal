package com.example.personalfinancemobile.app.data.model

import java.io.Serializable
import java.sql.Date

data class ServerDeposit(
    val id: Int,
    val date: String,
    val deposit: Int
) : Serializable
