package com.example.personalfinancemobile.activity.wellcome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Auth.LoginActivity
import com.example.personalfinancemobile.activity.Auth.RegisterActivity
import com.example.personalfinancemobile.activity.Budget.BudgedSchedulingActivity
import com.example.personalfinancemobile.activity.Budget.CategoriActivity
import com.example.personalfinancemobile.activity.MainActivity
import com.example.personalfinancemobile.activity.target.InputTargetActivity
import com.example.personalfinancemobile.app.ui.utils.SessionManager

class LandingPageActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_landing_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi SessionManager
        sessionManager = SessionManager(this)

        // Jika sudah login, langsung pindah ke MainActivity
        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val getStart = findViewById<Button>(R.id.btn_start)
        getStart.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}