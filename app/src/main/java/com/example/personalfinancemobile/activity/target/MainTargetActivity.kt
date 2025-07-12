package com.example.personalfinancemobile.activity.target

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.TargetResponse
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainTargetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_target)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sessionManager = SessionManager(this)
        val token = sessionManager.fetchAuthToken()

        val apiServices = RetrofitInstance.getInstance(this).create(APIServices::class.java)
        apiServices.getTarget("Bearer $token").enqueue(object : Callback<TargetResponse>{
            override fun onResponse(call: Call<TargetResponse?>, response: Response<TargetResponse?>) {
                if(response.isSuccessful) {
                    val targetList = response.body()
                    if (!targetList?.data.isNullOrEmpty()) {
                        val intent = Intent(this@MainTargetActivity, HomeTargetActivity::class.java)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this@MainTargetActivity, InputTargetActivity::class.java)
                        startActivity(intent)
                    }
                    finish()
                } else {
                    val errorBody = response.errorBody()?.toString()
                    Log.e("MainTargetActivityError", "Gagal Menapilkan Activity: $errorBody")
                    Toast.makeText(this@MainTargetActivity, "Coba lagi nanti", Toast.LENGTH_SHORT).show()
                }

            }
            override fun onFailure(call: Call<TargetResponse?>, t: Throwable) {
                Log.e("MainTargetActivy", "Error: ${t.message}")
                Toast.makeText(this@MainTargetActivity, "Erorr: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}