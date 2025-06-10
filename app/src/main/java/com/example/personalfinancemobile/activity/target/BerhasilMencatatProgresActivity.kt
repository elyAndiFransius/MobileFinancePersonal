package com.example.personalfinancemobile.activity.target

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.TargetResponse
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.utils.SessionManager // Pastikan import ini benar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BerhasilMencatatProgresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_berhasil_mencatat_progres)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val apiService = RetrofitInstance.getInstance(this).create(APIServices::class.java)
        val sessionManager = SessionManager(this)
        val token = "Bearer " + sessionManager.fetchAuthToken() // Perbaikan di sini

        apiService.getTarget(token).enqueue(object : Callback<TargetResponse> {
            override fun onResponse(call: Call<TargetResponse>, response: Response<TargetResponse>) {
                if (response.isSuccessful) {
                    val targets = response.body()?.data
                    // TODO: Lakukan sesuatu dengan data targets
                } else {
                    Toast.makeText(this@BerhasilMencatatProgresActivity,
                        "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TargetResponse>, t: Throwable) {
                Toast.makeText(this@BerhasilMencatatProgresActivity,
                    "Gagal Menampilkan data: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}