package com.example.personalfinancemobile.activity.wellcome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Auth.RegisterActivity
import com.example.personalfinancemobile.activity.Budget.BudgedSchedulingActivity
import com.example.personalfinancemobile.activity.Budget.CategoriActivity

class LandingPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_landing_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val get_start = findViewById<Button>(R.id.btn_start)

        get_start.setOnClickListener{
            val intent = Intent(this, BudgedSchedulingActivity::class.java)
            startActivity(intent)
        }
    }
}