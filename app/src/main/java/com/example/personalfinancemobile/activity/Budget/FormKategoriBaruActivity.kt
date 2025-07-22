package com.example.personalfinancemobile.activity.Budget

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.ui.utils.setupBackButton

class FormKategoriBaruActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form_kategori_baru)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val id_category = findViewById<EditText>(R.id.id_category)
        val backButton = findViewById<ImageView>(R.id.id_back)
        setupBackButton(this, backButton)

        val btnSave = findViewById<AppCompatButton>(R.id.btnSave)

        btnSave.setOnClickListener {
            val categoryString = id_category.text.toString().trim()

            if (categoryString.isBlank()) {
                Toast.makeText(this@FormKategoriBaruActivity, "Mohon isi semua inputan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                putExtra("kategori_nama", categoryString)
            }
            setResult(RESULT_OK, resultIntent)
            finish()

        }

    }

}