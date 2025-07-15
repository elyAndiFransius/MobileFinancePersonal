package com.example.personalfinancemobile.app.data.model

import androidx.core.app.NotificationCompat.MessagingStyle.Message
import java.util.Date
// model sudah gak di pakai
data class Target(
    val gol: String,
    val targetAmount: Int,
    val currentAmount: Int,
    val startDate: Date,
    val endDate: Date,
    val file: String,
    )

data class TargetResponse(
    val success: Boolean,
    val message: String,
    val data: List<TargetResponseId>
)

data class TargetResponseId (
    val id: Int,
    val gol: String,
    val targetAmount: Int,
    val currentAmount: Int,
    val startDate: Date,
    val endDate: Date,
    val file: String,
)
