package com.example.personalfinancemobile.activity.Transaksi

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import java.text.SimpleDateFormat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import java.util.Calendar
import com.example.personalfinancemobile.app.data.repository.CategoryProvider
import java.util.Locale

class AddTransactionActivity : AppCompatActivity() {
    private var  selectedCategoryId: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_transaction)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Untuk masukkan Type Transaction
        val id_type = findViewById<AutoCompleteTextView>(R.id.id_type)
        val types = listOf("Income", "Expense")

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, types)
        id_type.setAdapter(adapter)

        id_type.setOnClickListener {
            id_type.showDropDown()
        }

        // Untuk masukkan Category Transaction
        val categoryField = findViewById<EditText>(R.id.id_category)

        categoryField.setOnClickListener {
            showCategoryDialog()
        }

        // Untuk memasukkan Date Transcation
        val date = findViewById<EditText>(R.id.id_Date)
        val calender = Calendar.getInstance()

        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

        date.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)
                    date.setText(dateFormat.format(selectedDate.time))
                },
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH),
            ).show()
        }
        }
        private fun showCategoryDialog() {
            val categories = CategoryProvider.getDefaultCategories()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose Category")

            val categoryViews = categories.map { category ->
                "${category.name}"
            }.toTypedArray()

            builder.setItems(categoryViews) { dialog, which ->
                val selectedCategory = categories[which]
                findViewById<EditText>(R.id.id_category).setText(selectedCategory.name)

                //Simpan juga ID kalau di butuhkan untuk di kirim ke serve nantinya
                selectedCategoryId = selectedCategory.id
            }
            builder.setNegativeButton("Cancel", null)
            builder.show()
        }
    }

