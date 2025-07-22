package com.example.personalfinancemobile.app.ui.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale


class NumberFormat (private val editText: EditText) : TextWatcher {
    private var current = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

    override fun afterTextChanged(s: Editable?) {
        if (s.toString() != current) {
            editText.removeTextChangedListener(this)

            val cleanString = s.toString().replace(".", "").replace(",", "")
            if (cleanString.isNotEmpty()) {
                try {
                    val parsed = cleanString.toLong()
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val formatted = formatter.format(parsed)

                    current = formatted
                    editText.setText(formatted)
                    editText.setSelection(formatted.length)
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }

            editText.addTextChangedListener(this)
        }
    }
}