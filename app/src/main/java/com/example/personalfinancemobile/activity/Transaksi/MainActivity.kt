package com.example.personalfinancemobile.activity.Transaksi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R

class MainActivity : AppCompatActivity() {
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

        // ✅ Buat instance ExpenseFragment dengan arguments kosong
        val expenseFragment = ExpenseFragment()
        expenseFragment.arguments = Bundle()

        // ✅ Default Fragment (Income)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmant_layout, IncomeFragment())
            .commit()
        Log.d("MainActivity", "Menampilkan IncomeFragment sebagai default")


        btnIncome.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmant_layout, IncomeFragment())
                .commit()
        }

        btnExpense.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmant_layout, expenseFragment) // ✅ gunakan yang sudah diberi arguments
                .commit()
        }
        btnAddTrans.setOnClickListener {
            addTransaction()
        }
    }
    private fun addTransaction() {
        val intent = Intent(this@MainActivity, AddTransactionActivity::class.java)
        startActivity(intent)
    }

}
