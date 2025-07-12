package com.example.personalfinancemobile.activity.Budget

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.BudgetRequest
import com.example.personalfinancemobile.app.data.model.CategoryRequest
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.data.repository.CategoryProvider
import com.example.personalfinancemobile.app.ui.adapter.Category
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CategoryTotalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_total)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val priode = intent.getStringExtra("priode") ?: "harian"
        val parcelableArray = intent.getParcelableArrayExtra("Kategory")
        val selectedCategories = parcelableArray?.filterIsInstance<Category>()
        val recommended = CategoryProvider.getRecomendedAllocation()
        val pemasukkan = intent.getIntExtra("jumlah", 0)


        // pisahakan kategori berdasarlakan rekomeded dan inputan user

        val (recommendedCategories, userDefindCategories) = selectedCategories.orEmpty().partition {
            recommended.containsKey(it.name)
        }

        //  Hitung total persentasi kategori dari provider
        val userPersen = recommendedCategories.sumOf { recommended[it.name] ?: 0 }
        val remainingPersen = 100 - userPersen

        // Hitung total persentasi kategori yang di inputkan oleh user
        val eddPersen = if (userDefindCategories.isNotEmpty()) {
            remainingPersen / userDefindCategories.size
        } else 0

        // gabungkan kedua kategori
        val categoryAllocation = mutableMapOf<String, Int>()

        recommendedCategories.forEach {
            categoryAllocation[it.name] = recommended[it.name] ?: 0
        }

        userDefindCategories.forEach {
            categoryAllocation[it.name] = eddPersen
        }

        val container = findViewById<LinearLayout>(R.id.categoryContainer)
        val categoryRequests = mutableListOf<CategoryRequest>()
        val totalBudget = pemasukkan

        val btnSave = findViewById<AppCompatButton>(R.id.btnSave)
        val btnReset = findViewById<AppCompatButton>(R.id.btnReset)

        // Untuk mereset Budget
        btnReset.setOnClickListener {
            val intent = Intent(this@CategoryTotalActivity, BudgedSchedulingActivity::class.java)
            startActivity(intent)
        }

        // Untuk Menampilkan Total Semua Kategory yang di pilih
        selectedCategories?.forEach { category ->
            // Untuk melakukan opreasi pemabagian
            val persen = categoryAllocation[category.name] ?: 0
            val totalCategori = totalBudget * persen / 100

            categoryRequests.add(
                CategoryRequest(
                    name = category.name,
                    jumlah = totalCategori
                )
            )

            // Tambahkan ke daftar Budged
            val itemView = LayoutInflater.from(this)
                .inflate(R.layout.total_jumlah_kategori, null)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = 10
            itemView.layoutParams = params

            val txtName = itemView.findViewById<TextView>(R.id.txtCategoryName)
            val imgIcon = itemView.findViewById<ImageView>(R.id.imgCategory)
            val jumlah = itemView.findViewById<TextView>(R.id.id_jumlah)

            // Untuk menampilkan category dan jumlah kedalam Tampilan
            txtName.text = "${category.name}"
            jumlah.text = "${totalCategori}"
            imgIcon.setImageResource(category.image)
            container.addView(itemView)

        }
        val sessionManager = SessionManager(this)
        val token = sessionManager.fetchAuthToken()
        // Untuk menampung Request yang di inputkan user
        val budgetToSend = BudgetRequest(
            pemasukkan = pemasukkan,
            priode = priode,
            categories = categoryRequests
        )
        // Untuk Memasukan semua data yang ada di dalam list BudgetTosend
        btnSave.setOnClickListener {
            val BudgetService = RetrofitInstance.getInstance(this).create(APIServices::class.java)
            BudgetService.createBudgetRequest(budgetToSend, "Bearer $token").enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Berhasil()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("BudgetError", "Wah gagal bro: $errorBody")
                        Toast.makeText(this@CategoryTotalActivity, "yah, gagal di rekap: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@CategoryTotalActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("BudgetError", "Throwable: ${t.message}")
                }
            })
        }
    }
    private fun Berhasil() {
        val intent = Intent(this@CategoryTotalActivity, BerhasilBudgetingActivity::class.java)
        startActivity(intent)
        finish()
    }
}