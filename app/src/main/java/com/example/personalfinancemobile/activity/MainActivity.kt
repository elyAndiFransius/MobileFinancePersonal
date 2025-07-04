package com.example.personalfinancemobile.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Auth.LoginActivity
import com.example.personalfinancemobile.activity.Transaksi.MainActivity as Transaksi
import com.example.personalfinancemobile.activity.Budget.MainBudgetingActivity
import com.example.personalfinancemobile.activity.target.HomeTargetActivity
import com.example.personalfinancemobile.activity.target.MainTargetActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val budget = findViewById<ImageView>(R.id.ic_budget)
        val record = findViewById<ImageView>(R.id.ic_record)
        val spanding = findViewById<ImageView>(R.id.ic_spending)

        val tx_budget = findViewById<TextView>(R.id.tx_budget)
        val tx_record = findViewById<TextView>(R.id.tx_record)
        val tx_spanding = findViewById<TextView>(R.id.tx_spending)

        val account = findViewById<ImageView>(R.id.account)

        account.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        //untuk tombol budget
        budget.setOnClickListener{
            budget()
        }
        tx_budget.setOnClickListener{
            budget()
        }
        //untuk tombol records
        record.setOnClickListener{
            records()
        }
        tx_record.setOnClickListener{
            records()
        }
        //untuk tombol spanding
        spanding.setOnClickListener{
            spanding()
        }
        tx_spanding.setOnClickListener{
            spanding()
        }

    }
    private fun budget(){
        val intent= Intent(this@MainActivity, MainBudgetingActivity::class.java)
        startActivity(intent)
    }
    private fun records(){
        val intent = Intent(this@MainActivity, Transaksi::class.java)
        startActivity(intent)
    }
    private fun spanding(){
        val intent = Intent(this@MainActivity, MainTargetActivity::class.java)
        startActivity(intent)

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