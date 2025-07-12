package com.example.personalfinancemobile.activity.Budget

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.personalfinancemobile.app.data.model.BudgetingResponse
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainBudgetingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sessionManager = SessionManager(this)
        val token = sessionManager.fetchAuthToken()
        val apiService = RetrofitInstance.getInstance(this).create(APIServices::class.java)

        apiService.budgetingIndex("Bearer $token").enqueue(object : Callback<BudgetingResponse> {
            override fun onResponse(
                call: Call<BudgetingResponse?>,
                response: Response<BudgetingResponse?>
            ) {
                if (response.isSuccessful){
                   val budgetList = response.body()
                    if (!budgetList?.data.isNullOrEmpty()) {
                        val intent = Intent(this@MainBudgetingActivity, ListBudgetActivity::class.java)
                        startActivity(intent)
                    }else {
                        val intent = Intent(this@MainBudgetingActivity, BudgedSchedulingActivity::class.java)
                        startActivity(intent)
                    }
                    finish()
                }else{
                    Toast.makeText(this@MainBudgetingActivity, "Gagal memuat Data", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<BudgetingResponse?>, t: Throwable) {
                Log.e("MainBudgetingActivity", "Error: ${t.message}")
                Toast.makeText(this@MainBudgetingActivity, "Coba lagi nnanti", Toast.LENGTH_SHORT).show()
            }
        })

    }
}