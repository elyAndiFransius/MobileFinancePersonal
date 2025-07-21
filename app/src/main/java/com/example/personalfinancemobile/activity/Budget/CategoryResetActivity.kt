package com.example.personalfinancemobile.activity.Budget

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
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
import com.example.personalfinancemobile.app.ui.adapter.Category
import com.example.personalfinancemobile.app.data.model.CategoryRequest
import com.example.personalfinancemobile.app.data.model.Priode
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.data.repository.CategoryProvider
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryResetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_reset)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val parcelableArray = intent.getParcelableArrayExtra("Kategory")
        val selectedCategories = parcelableArray?.filterIsInstance<Category>()
        val recommended = CategoryProvider.getRecomendedAllocation()
        val pemasukkan = intent.getIntExtra("jumlah", 0)
        val priode = (intent.getSerializableExtra("priode") as? Priode)?.name ?: ""





        // Pisakan ketegori berdasarkan apakah list rekomendasi
        val (recommendedCategories, userDefindCategories) = selectedCategories.orEmpty().partition {
            recommended.containsKey(it.name)
        }

        //  Hitung total persentasi kategori dari provider
        val userPersen = recommendedCategories.sumOf { recommended[it.name] ?: 0 }
        val remainingPersent = 100 - userPersen

        // Hitung total persentasi kategori yang di inputkan oleh user
        val addPersen = if (userDefindCategories.isNotEmpty()) {
            remainingPersent / userDefindCategories.size
        } else 0

        // gabungkan kedua kategori
        val categoryAllocation = mutableMapOf<String, Int>()

        recommendedCategories.forEach {
            categoryAllocation[it.name] = recommended[it.name] ?: 0
        }

        userDefindCategories.forEach {
            categoryAllocation[it.name] = addPersen
        }

        val totalBudget = pemasukkan
        val contrain = findViewById<LinearLayout>(R.id.categoryContainer)
        val btnSave = findViewById<AppCompatButton>(R.id.btnSave)
        val inputFields = mutableListOf<Pair<String, EditText>>() // nama kategori dan input jumlah


        selectedCategories?.forEach {  category ->
            val persen = categoryAllocation[category.name] ?: 0
            val totalCategori = totalBudget * persen / 100


            val  itemView = LayoutInflater.from(this)
                .inflate(R.layout.input_jumlah_kategori, null)


            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = 16
            itemView.layoutParams = params

            val txtCategoryName = itemView.findViewById<TextView>(R.id.txtCategoryName)
            val id_jumlah = itemView.findViewById<EditText>(R.id.id_jumlah)
            val imgIcon  = itemView.findViewById<ImageView>(R.id.imgCategory)

            txtCategoryName.text = "${category.name}"
            imgIcon.setImageResource(category.image)
            id_jumlah.hint = "${totalCategori}"
            contrain.addView(itemView)

            inputFields.add(Pair(category.name, id_jumlah))

        }

        val sessionManager = SessionManager(this)
        val token = sessionManager.fetchAuthToken()
        // Untuk menampung Request yang di inputkan user

        // Untuk Memasukan semua data yang ada di dalam list BudgetTosend
        btnSave.setOnClickListener {
            val categoryRequests = mutableListOf<CategoryRequest>()

            inputFields.forEach { (name, editText) ->
                val inputText = editText.text.toString()
                val jumlah = if (inputText.isNotEmpty()) {
                    inputText.toIntOrNull() ?: 0 // Kalau user isi tapi bukan angka, pakai 0
                } else {
                    editText.hint.toString().toIntOrNull() ?: 0
                }
                categoryRequests.add(CategoryRequest(name, jumlah))
            }
            val budgetToSend = BudgetRequest(
                pemasukkan = pemasukkan,
                priode = priode,
                categories = categoryRequests
            )

            val BudgetService = RetrofitInstance.getInstance(this).create(APIServices::class.java)
            BudgetService.createBudgetRequest(budgetToSend, "Bearer $token").enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Berhasil()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("BudgetError", "Wah gagal bro: $errorBody")
                        Toast.makeText(this@CategoryResetActivity, "yah, gagal di rekap: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@CategoryResetActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("BudgetError", "Throwable: ${t.message}")
                }
            })
        }
    }
    private fun Berhasil() {
        val intent = Intent(this@CategoryResetActivity, BerhasilBudgetingActivity::class.java)
        startActivity(intent)
        finish()
    }
}