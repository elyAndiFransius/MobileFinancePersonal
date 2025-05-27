package com.example.personalfinancemobile.activity.Budget

import android.os.Bundle
import android.telecom.CallScreeningService.CallResponse
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.Budget
import com.example.personalfinancemobile.app.data.model.BudgetRequest
import com.example.personalfinancemobile.app.data.model.CategoryRequest
import com.example.personalfinancemobile.app.data.model.Priode
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.ui.adapter.Category
import okhttp3.ResponseBody
import com.example.personalfinancemobile.app.data.model.Category as ModelCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TotalCategoryPersenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_total_category_persen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val priode = intent.getStringExtra("priode") ?: "harian"
        val pemasukkan = intent.getIntExtra("Jumlah", 0)
        val parcelableArray = intent.getParcelableArrayExtra("Kategory")
        val selectedCategories = parcelableArray?.filterIsInstance<Category>()

        val categoryAllocation = mapOf(
            "Transport" to 20,
            "Food" to 30,
            "Entertain" to 30,
            "Clothes" to 10,
            "Savings" to 5,
            "Miscellaneous" to 5
        )

        val categoryRequests = mutableListOf<CategoryRequest>()

        val container = findViewById<LinearLayout>(R.id.categoryContainer)
        val totalBudget = pemasukkan
        val priodee = priode

        selectedCategories?.forEach { category ->
            val persen = categoryAllocation[category.name] ?: 0
            val jumlah = totalBudget * persen / 100


            categoryRequests.add(
                CategoryRequest(
                    name = category.name,
                    jumlah = jumlah
                )
            )
            // Tambahkan ke daftar Budge
            val itemView = LayoutInflater.from(this)
                .inflate(R.layout.item_kategori, null)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = 16
            itemView.layoutParams = params

            val txtName = itemView.findViewById<TextView>(R.id.txtCategoryName)
            val imgIcon = itemView.findViewById<ImageView>(R.id.imgCategory)
            val bntAdd = itemView.findViewById<ImageView>(R.id.id_add)

            txtName.text = "${category.name}  \t Rp.${jumlah}"
            imgIcon.setImageResource(category.image)
            bntAdd.visibility = View.GONE
            container.addView(itemView)

        }
        val budgetToSend = BudgetRequest(
            pemasukkan = pemasukkan,
            priode = priode,
            categories = categoryRequests
        )

        // kirim ke server
        val BudgetService = RetrofitInstance.instance.create(APIServices::class.java)
        BudgetService.createBudgetRequest(budgetToSend).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@TotalCategoryPersenActivity, "Mantap sudah di rekap!!", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("BudgetError", "Wah gagal bro: $errorBody")
                    Toast.makeText(this@TotalCategoryPersenActivity, "yah, gagal di rekap: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@TotalCategoryPersenActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("BudgetError", "Throwable: ${t.message}")
            }
        })
    }
}