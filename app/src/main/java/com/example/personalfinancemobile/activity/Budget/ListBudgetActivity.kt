package com.example.personalfinancemobile.activity.Budget

import android.content.Intent
import android.os.Bundle
import retrofit2.Call
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.MainActivity
import com.example.personalfinancemobile.activity.Transaksi.IncomeFragment
import com.example.personalfinancemobile.app.data.model.BudgetRequest
import com.example.personalfinancemobile.app.data.model.BudgetingResponse
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.utils.SessionManager
import retrofit2.Callback
import retrofit2.Response

class ListBudgetActivity : AppCompatActivity() {
    companion object {
        var allBudget: List<BudgetRequest> = emptyList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_budget)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        IndexBudget()
        val btnBack = findViewById<AppCompatButton>(R.id.btnBack)
        val btnReset = findViewById<AppCompatButton>(R.id.btnReset)

        btnBack.setOnClickListener {
            Home()
        }
    }
    private fun IndexBudget() {
        val sessionManager = SessionManager(this)
        val token = sessionManager.fetchAuthToken()

        val apiService = RetrofitInstance.getInstance(this).create(APIServices::class.java)
        apiService.budgetingIndex("Bearer $token").enqueue(object : Callback<BudgetingResponse>{
            override fun onResponse(call: Call<BudgetingResponse>,
                                    response: Response<BudgetingResponse>
            ) {
                if (response.isSuccessful){
                    val data = response.body()
                    allBudget = data?.data ?: emptyList()
                    Log.d("ListBudgetActivity", "Budgeting: $data")

                    val categories = allBudget.flatMap { it.categories }
                    val fragment = CategoryFragment.newInstance(categories)

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.category_layout, fragment)
                        .commit()
                }

            }
            override fun onFailure(call: Call<BudgetingResponse>, t : Throwable) {
                Log.e("ListBudgetActivity", "Error: ${t.message}")
                Toast.makeText(this@ListBudgetActivity, "Coba lagi nati", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun Home() {
        val intent = Intent(this@ListBudgetActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

}