package com.example.personalfinancemobile.activity.Transaksi

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.TransactionModel

class EditTransactionActivity : AppCompatActivity() {
    private lateinit var transaction: TransactionModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_transaction)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Ambil data dari intent
        transaction = intent.getSerializableExtra("transaction") as TransactionModel

        // Ambil view
        val etDeskripsi = findViewById<EditText>(R.id.etDeskripsi)
        val etJumlah = findViewById<EditText>(R.id.etJumlah)
        val etTanggal = findViewById<EditText>(R.id.etTanggal)
        val etJenis = findViewById<EditText>(R.id.etJenis)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        // Isi form dengan data dari intent
        etDeskripsi.setText(transaction.descripsi)
        etJumlah.setText(transaction.jumlah.toString())
        etTanggal.setText(transaction.date)
        etJenis.setText(transaction.jenis)

        // Saat tombol simpan diklik
        btnSimpan.setOnClickListener {
            val updatedDeskripsi = etDeskripsi.text.toString()
            val updatedJumlah = etJumlah.text.toString().toIntOrNull() ?: 0
            val updatedTanggal = etTanggal.text.toString()
            val updatedJenis = etJenis.text.toString()

            // Buat data baru hasil edit
            val updatedTransaction = TransactionModel(
                jenis = updatedJenis,
                date = updatedTanggal,
                descripsi = updatedDeskripsi,
                jumlah = updatedJumlah
            )


        }
    }
}