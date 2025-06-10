package com.example.personalfinancemobile.activity.wellcome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Auth.LoginActivity
import com.example.personalfinancemobile.activity.Auth.RegisterActivity
import com.example.personalfinancemobile.app.data.model.Auth.Constants
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
        // SherePrefences
        val sharePref = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE)
        val token = sharePref.getString(Constants.TOKEN_KEY, null)
        Log.d("SplashScreen", "Token yang ditemukan: $token")


        lifecycleScope.launch {
        delay(2000)
        if (token != null) {
            // Sudah pernah login dan token nya ada
            val intent = Intent(this@SplachScreenActivity, LandingPageActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this@SplachScreenActivity, LoginActivity::class.java)
            startActivity(intent)

        }
        finish()
    }
    }
}