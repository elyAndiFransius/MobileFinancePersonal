package com.example.personalfinancemobile.activity.target

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.personalfinancemobile.activity.Deposit.DepoMainActivity
import com.example.personalfinancemobile.activity.target.AddProgresTargetActivity
import com.example.personalfinancemobile.app.data.model.TargetResponse
import com.example.personalfinancemobile.app.data.model.TargetResponseId
import com.example.personalfinancemobile.app.data.model.TransactionResponse
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.ui.utils.NumberFormatText
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import okhttp3.ResponseBody
import okhttp3.internal.concurrent.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeTargetActivity : AppCompatActivity() {

    companion object {
        var allTarget: List<TargetResponseId> = emptyList()
    }

    private lateinit var btnReset: AppCompatButton
    private lateinit var btnRecord: AppCompatButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_target)

        btnRecord = findViewById(R.id.btnRecord)
        btnReset = findViewById(R.id.btnReset)
        btnReset.isEnabled = false // Awalnya disable

        btnRecord.setOnClickListener {
            AddProgresTargetActivity()
        }

        val sessionManager = SessionManager(this)
        val token = sessionManager.fetchAuthToken()

        // Muat data target
        loadTargetData("Bearer $token")

        btnReset.setOnClickListener {
            if (allTarget.isNotEmpty()) {
                val targetId = allTarget[0].id
                Popup(targetId)
            } else {
                Toast.makeText(this@HomeTargetActivity, "Tidak ada target untuk dihapus", Toast.LENGTH_SHORT).show()
            }
        }
        val depo = findViewById<AppCompatButton>(R.id.btnHistori)

        depo.setOnClickListener {
            val intent = Intent(this@HomeTargetActivity, DepoMainActivity::class.java)
            startActivity(intent)
        }

    }
    private fun formatDate (inputDate: Date): String {
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        return try {
            outputFormat.format(inputDate)
        } catch (e: Exception) {
            inputDate.toString() // fallback
        }
    }

    private fun loadTargetData(token: String) {
        val apiService = RetrofitInstance.getInstance(this).create(APIServices::class.java)
        apiService.getTarget(token).enqueue(object : Callback<TargetResponse> {
            override fun onResponse(call: Call<TargetResponse>, response: Response<TargetResponse>) {
                if (response.isSuccessful) {
                    allTarget = response.body()?.data ?: emptyList()
                    Log.d("HomeTargetActivity", "Target berhasil dimuat: ${allTarget.size}")

                    if (allTarget.isNotEmpty()) {
                        val target = allTarget[0]

                        // Tampilkan data langsung
                        val imageView = findViewById<ImageView>(R.id.ImgViewUrl)
                        Glide.with(this@HomeTargetActivity)
                            .load(target.file)
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_image_errror)
                            .into(imageView)

                        findViewById<TextView>(R.id.name_gol).text = target.gol
                        findViewById<TextView>(R.id.count_target_amount).text = "Rp ${NumberFormatText(target.targetAmount.toLong())}"
                        findViewById<TextView>(R.id.id_current_now).text = "Rp ${NumberFormatText(target.currentAmount.toLong())}"
                        findViewById<TextView>(R.id.start).text = formatDate(target.startDate)
                        findViewById<TextView>(R.id.end).text = formatDate(target.endDate)

                        btnReset.isEnabled = true
                    } else {
                        btnReset.isEnabled = false
                        Toast.makeText(this@HomeTargetActivity, "Belum ada data target", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@HomeTargetActivity, "Gagal memuat target: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TargetResponse>, t: Throwable) {
                Toast.makeText(this@HomeTargetActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun AddProgresTargetActivity() {
        val intent = Intent(this@HomeTargetActivity, AddProgresTargetActivity::class.java)
        startActivity(intent)
    }


    private fun Popup(targetId: Int) {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_reset_target, null)

        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.show()

        dialog.findViewById<AppCompatButton>(R.id.btnYes)?.setOnClickListener {
            val sessionManager = SessionManager(this)
            val token = sessionManager.fetchAuthToken()

            val apiService = RetrofitInstance.getInstance(this).create(APIServices::class.java)
            apiService.deleteTarget(targetId, "Bearer $token").enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@HomeTargetActivity, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@HomeTargetActivity, InputTargetActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("DeleteError", "Gagal menghapus data target: $errorBody")
                        Toast.makeText(this@HomeTargetActivity, "Gagal: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(this@HomeTargetActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("Delete Error", "Throwable: ${t.message}")
                }
            })
        }

        dialog.findViewById<AppCompatButton>(R.id.btnNo)?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(
            android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT)
        )
    }
}
