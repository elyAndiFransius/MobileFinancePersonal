package com.example.personalfinancemobile.activity.Deposit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Transaksi.MainActivity
import com.example.personalfinancemobile.app.data.model.DepositResponse
import com.example.personalfinancemobile.app.data.model.ServerDeposit
import com.example.personalfinancemobile.app.data.model.TransactionModel
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

class DepoMainActivity : AppCompatActivity() {

    companion object {
        var allDeposit: List<ServerDeposit> = emptyList()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_depo_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        IndexDepo()


    }

    private fun IndexDepo() {
        val sessionManager = SessionManager(this)
        val token = sessionManager.fetchAuthToken()

        val apiServices = RetrofitInstance.getInstance(this).create(APIServices::class.java)
        apiServices.indexDeposit("Bearer $token").enqueue(object : Callback<DepositResponse>{
            override fun onResponse(call: Call<DepositResponse>,
                                    response: Response<DepositResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    allDeposit = data?.data ?: emptyList()
                    Log.d("DepoMainActivityasdas", "Depo: $data")
                    Toast.makeText(this@DepoMainActivity, "Data Deposit", Toast.LENGTH_SHORT).show()

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmant_layout, DepositFragment())
                        .commit()
                    Log.d("DepoMainActivity", "Menampilkan DepoFragment sebagai default")
                } else {
                    Log.e("DepoMainActivity", "Gagal fetch depo: ${response.errorBody()?.string()}")
                }

            }

            override fun onFailure(call: Call<DepositResponse?>, t: Throwable) {
                Log.e("DepoMainActivity", "Error: ${t.message}")
                Toast.makeText(this@DepoMainActivity, "Coba lagi nanti", Toast.LENGTH_SHORT).show()
            }


        })
    }
}