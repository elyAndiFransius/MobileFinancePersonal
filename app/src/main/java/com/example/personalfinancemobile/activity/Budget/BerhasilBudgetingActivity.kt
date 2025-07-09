package com.example.personalfinancemobile.activity.Budget

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Budget.CategoryResetActivity
import com.example.personalfinancemobile.activity.MainActivity

class BerhasilBudgetingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_berhasil_budgeting)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnHome = findViewById<AppCompatButton>(R.id.btnHome)

        btnHome.setOnClickListener {
            val intent = Intent(this@BerhasilBudgetingActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}