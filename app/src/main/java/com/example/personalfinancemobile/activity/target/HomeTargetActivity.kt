package com.example.personalfinancemobile.activity.target

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.TargetResponse
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeTargetActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_target)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRecord = findViewById<AppCompatButton>(R.id.btnRecord)
        val btnReset = findViewById<AppCompatButton>(R.id.btnReset)


        btnRecord.setOnClickListener {
            AddProgresTargetActivity()
        }

        // Inisialisasi SessionManager
        sessionManager = SessionManager(this)

        // Cek status login terlebih dahulu
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Anda harus login terlebih dahulu", Toast.LENGTH_SHORT).show()
            finish() // Tutup activity dan kembali ke halaman sebelumnya
            return
        }

        // Ambil token dan validasi
        val token = sessionManager.fetchAuthToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token tidak ditemukan, silakan login ulang", Toast.LENGTH_SHORT).show()
            sessionManager.logout() // Hapus session yang rusak
            finish()
            return
        }
        loadTargetData("Bearer $token")
    }
    private fun loadTargetData(authToken: String) {
        val apiService = RetrofitInstance.getInstance(this).create(APIServices::class.java)
        apiService.getTarget(authToken).enqueue(object : Callback<TargetResponse> {
            override fun onResponse(call: Call<TargetResponse>, response: Response<TargetResponse>) {
                if (response.isSuccessful) {
                    val targets = response.body()?.data

                    if (!targets.isNullOrEmpty()) {
                        val target = targets[0] // ambil satu data pertama
                        // Isi ke ImageView
                        val imageView = findViewById<ImageView>(R.id.ImgViewUrl)
                        Glide.with(this@HomeTargetActivity)
                            .load(target.file) // URL gambar dari server
                            .placeholder(R.drawable.ic_placeholder) // opsional: gambar loading
                            .error(R.drawable.ic_image_errror) // opsional: jika gagal
                            .into(imageView)

                        // Isi ke TextView
                        findViewById<TextView>(R.id.name_gol).text = target.gol
                        findViewById<TextView>(R.id.count_target_amount).text = "Rp ${target.targetAmount}"
                        findViewById<TextView>(R.id.id_current_now).text = "Rp ${target.currentAmount}"
                        findViewById<TextView>(R.id.start).text = formatDate(target.startDate)
                        findViewById<TextView>(R.id.end).text = formatDate(target.endDate)

                        Toast.makeText(this@HomeTargetActivity, "Data target berhasil dimuat", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@HomeTargetActivity, "Data target kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle different error codes
                    when (response.code()) {
                        401 -> {
                            Toast.makeText(this@HomeTargetActivity,
                                "Session expired, silakan login ulang", Toast.LENGTH_SHORT).show()
                            sessionManager.logout()
                            finish()
                        }
                        404 -> {
                            Toast.makeText(this@HomeTargetActivity,
                                "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this@HomeTargetActivity,
                                "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<TargetResponse>, t: Throwable) {
                Toast.makeText(this@HomeTargetActivity,
                    "Gagal menampilkan data: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun formatDate (inputDate: Date): String {
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        return try {
            outputFormat.format(inputDate)
        } catch (e: Exception) {
            inputDate.toString() // fallback
        }
    }
    private fun AddProgresTargetActivity() {
        val intent = Intent(this@HomeTargetActivity, AddProgresTargetActivity::class.java)
        startActivity(intent)
    }

    private fun Popup () {
        // Ambil tampilan dari layout
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_reset_target, null)

        // Buat dialog Pop Up
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Tampilkan dialog kedalam layar
        dialog.show()


        // untuk batal mengahapus
        dialog.findViewById<AppCompatButton>(R.id.btnNo)?.setOnClickListener {
            dialog.dismiss()
        }

        // Hapus background putih di luar sudut layout
        dialog.window?.setBackgroundDrawable(
            android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT)
        )

    }
}