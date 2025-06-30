package com.example.personalfinancemobile.activity.Transaksi

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import java.text.SimpleDateFormat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.BudgetRequest
import com.example.personalfinancemobile.app.data.model.TransactionModel
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import java.util.Calendar
import com.example.personalfinancemobile.app.data.repository.CategoryProvider
import com.example.personalfinancemobile.utils.SessionManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Locale
import kotlin.String

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

        val type = findViewById<AutoCompleteTextView>(R.id.id_type)
        val jumlah = findViewById<EditText>(R.id.id_Amount)
        val categoryField = findViewById<EditText>(R.id.id_category)
        val date = findViewById<EditText>(R.id.id_Date)
        val desc = findViewById<EditText>(R.id.id_des)
        val btnSave = findViewById<AppCompatButton>(R.id.id_btn_save)

        // Untuk masukkan Type Transaction
        val types = listOf("Income", "Expense")

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, types)
        type.setAdapter(adapter)

        type.setOnClickListener {
            type.showDropDown()
        }

        // Untuk masukkan Category Transaction
        categoryField.setOnClickListener {
            showCategoryDialog()
        }
        // Untuk memasukkan Date Transcation
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

        btnSave.setOnClickListener {
            val jenisString = when (type.text.toString()) {
                "Income" -> "pemasukkan"
                "Expense" -> "pengeluaran"
                else -> ""
            }
            val jumlahString  = jumlah.text.toString().trim()
            val dateString = date.text.toString().trim()
            val descString = desc.text.toString().trim()
            val categoryId = selectedCategoryId

            if (jenisString.isBlank()  ||  descString.isBlank()  || jumlahString.isBlank() ||  dateString.isBlank() ) {
                Toast.makeText(this@AddTransactionActivity, "Mohon isi semua Inputan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val sessionManager = SessionManager(this)
            val token = sessionManager.fetchAuthToken()
            val apiService = RetrofitInstance.getInstance(this).create(APIServices::class.java)

            apiService.createTransaction(
                categoryId.toString(), jenisString, descString,  jumlahString, dateString, "Bearer $token"
            ).enqueue(object : Callback<ResponseBody> {
                override fun onResponse( call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                    if(response.isSuccessful) {
                        Toast.makeText(this@AddTransactionActivity, "Transaction Berhasil di Simpan", Toast.LENGTH_SHORT).show()
                        navigateToMainActivity()
                    }else{
                        val errorBody = response.errorBody()?.string()
                        Log.e("TransactionError", "Transaction gagal dibuat: $errorBody")
                        Log.d("DEBUG", "Category category yang dikirim: ${jenisString}")
                        Toast.makeText(this@AddTransactionActivity, "Coba lagi", Toast.LENGTH_SHORT).show()

                        }
                    }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(this@AddTransactionActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("TransactionError", "Throwable: ${t.message}")

                    }

                })
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

    private fun navigateToMainActivity(){
        val intent = Intent(this@AddTransactionActivity, MainActivity::class.java)
        startActivity(intent)
    }

}

