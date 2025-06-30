package com.example.personalfinancemobile.activity.Transaksi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.TransactionModel
import com.example.personalfinancemobile.app.data.model.TransactionResponse
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.utils.SessionManager
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale
import okhttp3.ResponseBody
import retrofit2.Call


class MainActivity : AppCompatActivity() {
    companion object {
        var allTransaction: List<TransactionModel> = emptyList()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnIncome = findViewById<AppCompatButton>(R.id.btnIncome)
        val btnExpense = findViewById<AppCompatButton>(R.id.btnExpense)
        val btnAddTrans = findViewById<AppCompatButton>(R.id.btnAddTransc)

        // Panggil langsung
        IndexTransaction()

        //  Buat instance ExpenseFragment dengan arguments kosong
        val expenseFragment = ExpenseFragment()
        expenseFragment.arguments = Bundle()


        btnIncome.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmant_layout, IncomeFragment())
                .commit()
        }

        btnExpense.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmant_layout, expenseFragment) //  gunakan yang sudah diberi arguments
                .commit()
        }
        btnAddTrans.setOnClickListener {
            addTransaction()
        }
    }

    private fun IndexTransaction() {
        val sessionManager = SessionManager(this)
        val token = sessionManager.fetchAuthToken()

        val apiService = RetrofitInstance.getInstance(this).create(APIServices::class.java)
        apiService.indexTransaction("Bearer $token").enqueue(object: Callback<TransactionResponse> {
            override fun onResponse(call: Call<TransactionResponse>,
                                    response: Response<TransactionResponse>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    allTransaction = data?.data ?: emptyList()
                    Log.d("MainActivity", "Transaksi: $data")
                    Toast.makeText(this@MainActivity, "Mantap mas bro ini datanya", Toast.LENGTH_SHORT).show()

                    //  Default Fragment (Income)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmant_layout, IncomeFragment())
                        .commit()
                    Log.d("MainActivity", "Menampilkan IncomeFragment sebagai default")

                }else{
                    Log.e("MainActivity", "Gagal fetch transaksi: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Coba lagi nanti", Toast.LENGTH_SHORT).show()
            }

        })

    }
    private fun addTransaction() {
        val intent = Intent(this@MainActivity, AddTransactionActivity::class.java)
        startActivity(intent)
    }

}
