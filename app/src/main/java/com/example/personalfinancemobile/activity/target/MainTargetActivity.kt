package com.example.personalfinancemobile.activity.target

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.TargetResponse
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainTargetActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_target)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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

        // Panggil API
        loadTargetData("Bearer $token")
    }

    private fun loadTargetData(authToken: String) {
        val apiService = RetrofitInstance.getInstance(this).create(APIServices::class.java)

        // PERBAIKAN: Tambahkan generic type <TargetResponse>
        apiService.getTarget(authToken).enqueue(object : Callback<TargetResponse> {
            override fun onResponse(call: Call<TargetResponse>, response: Response<TargetResponse>) {
                if (response.isSuccessful) {
                    val targets = response.body()?.data

                    if (!targets.isNullOrEmpty()) {
                        val target = targets[0] // ambil satu data pertama

                        val fullUrl = "http://192.168.105.204:8000/" + target.file

                        // Isi ke ImageView
                        val imageView = findViewById<ImageView>(R.id.ImgViewUrl)
                        Glide.with(this@MainTargetActivity)
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

                        Toast.makeText(this@MainTargetActivity, "Data target berhasil dimuat", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainTargetActivity, "Data target kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle different error codes
                    when (response.code()) {
                        401 -> {
                            Toast.makeText(this@MainTargetActivity,
                                "Session expired, silakan login ulang", Toast.LENGTH_SHORT).show()
                            sessionManager.logout()
                            finish()
                        }
                        403 -> {
                            Toast.makeText(this@MainTargetActivity,
                                "Akses ditolak", Toast.LENGTH_SHORT).show()
                        }
                        404 -> {
                            Toast.makeText(this@MainTargetActivity,
                                "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                        500 -> {
                            Toast.makeText(this@MainTargetActivity,
                                "Server error, coba lagi nanti", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this@MainTargetActivity,
                                "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<TargetResponse>, t: Throwable) {
                Toast.makeText(this@MainTargetActivity,
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

}