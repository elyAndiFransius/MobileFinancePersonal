package com.example.personalfinancemobile.activity.Transaksi

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import java.text.SimpleDateFormat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.target.BerhasilMencatatProgresActivity
import com.example.personalfinancemobile.app.data.model.CategoryResponse
import com.example.personalfinancemobile.app.data.model.ServerCategory
import com.example.personalfinancemobile.app.data.model.TransactionModel
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import java.util.Calendar
import com.example.personalfinancemobile.app.data.repository.CategoryProvider
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import com.example.personalfinancemobile.app.ui.utils.setupBackButton
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class AddTransactionActivity : AppCompatActivity() {
    private var  selectedCategoryId: Int? = null
    private var  serverCategories: List<ServerCategory> = emptyList()
    private var isCategoryLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 🔹 Atur warna navigation bar sesuai warna form
        val formColor = ContextCompat.getColor(this, R.color.primary2)
        window.navigationBarColor = formColor

        // 🔹 Supaya ikon navigation bar tetap terlihat jelas (misalnya hitam di atas warna terang)
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
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
        val backButton = findViewById<ImageView>(R.id.id_back)
        setupBackButton(this, backButton)

        val mode = intent.getStringExtra("mode") // "edit" atau null
        val transactionData = intent.getSerializableExtra("transaction") as? TransactionModel

        if (mode == "edit" && transactionData != null) {
            jumlah.setText(transactionData.jumlah.toString())
            date.setText(transactionData.date.toString())
            desc.setText(transactionData.descripsi.toString())
        }

        // Untuk masukkan Type Transaction
        val types = listOf("Income", "Expense")

        // val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, types)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, types)
        type.setAdapter(adapter)

        type.setOnClickListener {
            type.showDropDown()
        }

        categoryField.setOnClickListener {
            if (isCategoryLoaded) {
                showCategoryDialog()
            } else {
                Toast.makeText(this, "Mohon tunggu, kategori sedang dimuat...", Toast.LENGTH_SHORT).show()
            }
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
            val jumlahString = jumlah.text.toString().trim()
            val dateString = date.text.toString().trim()
            val descString = desc.text.toString().trim()
            val categoryId = selectedCategoryId

            if (jenisString.isBlank() || descString.isBlank() || jumlahString.isBlank() || dateString.isBlank()) {
                Toast.makeText(
                    this@AddTransactionActivity,
                    "Mohon isi semua inputan",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            val sessionManager = SessionManager(this)
            val token = sessionManager.fetchAuthToken()
            val apiService = RetrofitInstance.getInstance(this).create(APIServices::class.java)

            //  Gantilah sesuai endpoint edit deposit kamu
            if (mode == "edit" && transactionData != null) {
                apiService.updateTransaction(
                    transactionData.id,
                    categoryId.toString(),
                    jenisString,
                    descString,
                    jumlahString,
                    dateString,
                    "Bearer $token"
                ).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@AddTransactionActivity,
                                "Transaction Berhasil di update",
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToMainActivity()
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("TransactionError", "Transaction gagal dibuat: $errorBody")
                            Log.d("DEBUG", "Category ID yang dikirim: $selectedCategoryId")
                            Toast.makeText(
                                this@AddTransactionActivity,
                                "Coba lagi",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(
                            this@AddTransactionActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("TransactionError", "Throwable: ${t.message}")

                    }

                })
            } else {

                apiService.createTransaction(
                    categoryId.toString(),
                    jenisString,
                    descString,
                    jumlahString,
                    dateString,
                    "Bearer $token"
                ).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@AddTransactionActivity,
                                "Transaction Berhasil di Simpan",
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToMainActivity()
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("TransactionError", "Transaction gagal dibuat: $errorBody")
                            Log.d("DEBUG", "Category ID yang dikirim: $selectedCategoryId")
                            Toast.makeText(
                                this@AddTransactionActivity,
                                "Coba lagi",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(
                            this@AddTransactionActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("TransactionError", "Throwable: ${t.message}")

                    }

                })
            }
        }

        val apiService = RetrofitInstance.getInstance(this).create(APIServices::class.java)
        val token = SessionManager(this).fetchAuthToken()

        apiService.getCategories("Bearer $token").enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.success) {
                        serverCategories = responseBody.data
                        isCategoryLoaded = true
                        Log.d("CATEGORY", "Kategori berhasil dimuat: ${serverCategories.size} item")
                    } else {
                        Log.e("CATEGORY", "Response tidak sesuai: $responseBody")
                    }
                } else {
                    Log.e("CATEGORY", "Gagal ambil kategori: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.e("CATEGORY", "Gagal koneksi: ${t.message}")
            }
        })



    }
    private fun showCategoryDialog() {
        if (serverCategories.isEmpty()) {
            Toast.makeText(this, "Kategori belum dimuat dari server", Toast.LENGTH_SHORT).show()
            return
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Kategori")

        val categoryNames = serverCategories.map { it.name }.toTypedArray()

        builder.setItems(categoryNames) { _, which ->
            val selectedCategory = serverCategories[which]
            findViewById<EditText>(R.id.id_category).setText(selectedCategory.name)
            selectedCategoryId = selectedCategory.id // ID dikirim ke server
        }

        builder.setNegativeButton("Batal", null)
        builder.show()
    }


    private fun navigateToMainActivity(){
        val intent = Intent(this@AddTransactionActivity, BerhasilMencatatProgresActivity::class.java)
        startActivity(intent)
        finish()
    }

}

