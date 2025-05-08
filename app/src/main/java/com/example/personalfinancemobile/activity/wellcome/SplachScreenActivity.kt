package com.example.personalfinancemobile.activity.wellcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Auth.RegisterActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplachScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splach_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lifecycleScope.launch {
            delay(2000)

            val intent = Intent(this@SplachScreenActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}