package com.example.personalfinancemobile.activity.Budget

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Auth.LoginActivity
import com.example.personalfinancemobile.app.data.model.Budget
import com.example.personalfinancemobile.app.data.model.Category
import com.example.personalfinancemobile.app.data.model.Priode

class BudgedSchedulingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budged_scheduling)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnNext = findViewById<AppCompatButton>(R.id.id_btn_Set)
        val jumlah = findViewById<EditText>(R.id.id_belance)
        val priodeOptions = listOf("Harian", "Mingguan", "Bulanan", "Tahunan")
        val autoComplate = findViewById<AutoCompleteTextView>(R.id.id_priode)
        val id_back = findViewById<ImageView>(R.id.id_back)

        id_back.setOnClickListener {
            showLogoutConfirmationDialog()
        }


        btnNext.setOnClickListener {
            val pemasukkanStr = jumlah.text.toString()
            val priodeStr = autoComplate.text.toString()

            if (pemasukkanStr.isNotEmpty() && priodeStr.isNotEmpty()) {
                val pemasukkan = pemasukkanStr.toInt()

                val priode = when (priodeStr) {
                    "Harian" -> Priode.Harian
                    "Mingguan" -> Priode.Mingguan
                    "Bulanan" -> Priode.Bulanan
                    "Tahunan" -> Priode.Tahunan
                    else -> Priode.Bulanan  // Jika tidak di antara di atas maka akan di kembalikan ke default Bulanan
                }
                val intent = Intent(this, CategoriActivity::class.java)
                intent.putExtra("pemasukkan", pemasukkan)
                intent.putExtra("priode", priode)
                startActivity(intent)
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, priodeOptions)
        autoComplate.setAdapter(adapter)
        //Untuk menampilkan drop-dwn priode
        autoComplate.setOnClickListener {
            autoComplate.showDropDown()
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Logout")
        builder.setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")


        builder.setPositiveButton("Ya, Keluar") { dialog, _ ->
            dialog.dismiss()

            // Tampilkan pesan logout
            Toast.makeText(this, "Sedang logout...", Toast.LENGTH_SHORT).show()

            // Panggil method logout
            LoginActivity.logout(this)
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        // Agar dialog tidak bisa di-dismiss dengan tap di luar
        builder.setCancelable(false)

        val dialog = builder.create()
        dialog.show()

        // Styling tombol (opsional)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(R.color.red))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(R.color.gray))
    }
}