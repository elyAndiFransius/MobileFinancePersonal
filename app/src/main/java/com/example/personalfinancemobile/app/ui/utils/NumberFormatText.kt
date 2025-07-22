package com.example.personalfinancemobile.app.ui.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

fun NumberFormatText(value: Long): String {
    val symbols = DecimalFormatSymbols(Locale("id", "ID")).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    val formatter: NumberFormat = DecimalFormat("#,###", symbols)
    return formatter.format(value)
}