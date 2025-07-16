package com.example.personalfinancemobile.activity.target

import android.app.DatePickerDialog
import android.content.Intent
import java.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.EditText
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
import com.example.personalfinancemobile.app.data.model.ServerDeposit
import com.example.personalfinancemobile.app.data.model.TargetResponse
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.text.toIntOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddProgresTargetActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_progres_target)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val date = findViewById<EditText>(R.id.id_date)
        val deposit = findViewById<EditText>(R.id.id_deposit)

        val mode = intent.getStringExtra("mode") // "edit" atau null
        val depositData = intent.getSerializableExtra("deposit") as? ServerDeposit

        if (mode == "edit" && depositData != null) {
            date.setText(depositData.date)
            deposit.setText(depositData.deposit.toString())
        }


        val btnSave = findViewById<AppCompatButton>(R.id.btnSave)
        val btnBack = findViewById<AppCompatButton>(R.id.btnBack)
        val depo = findViewById<TextView>(R.id.textView2)

        depo.setOnClickListener {
            val intent = Intent(this@AddProgresTargetActivity, DepoMainActivity::class.java)
            startActivity(intent)
        }


        // Kembali ke activy sebelumnya
        btnBack.setOnClickListener {
            val intent = Intent(this@AddProgresTargetActivity, HomeTargetActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Menampilkan Image yang ada di server
        showImage()

        // Untuk memasukkan date
        val calender = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        date.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selecetedDate = Calendar.getInstance()
                    selecetedDate.set(year, month, dayOfMonth)
                    date.setText(dateFormat.format(selecetedDate.time))
                },
                    calender.get(Calendar.YEAR),
                    calender.get(Calendar.MONTH),
                    calender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }



        // Simpan progres target
        btnSave.setOnClickListener {
            val dateString =  date.text.toString()
            val depositString = deposit.text.toString()
            val depositInt = depositString.toIntOrNull() ?: 0

            fun createPartFormString(value: String): RequestBody {
                return  value.toRequestBody("text/plain".toMediaTypeOrNull())
            }

            val datePart = createPartFormString(dateString)
            val depositPart = createPartFormString(depositInt.toString())
            val sessionManager = SessionManager(this)
            val token = sessionManager.fetchAuthToken()

            val apiServices = RetrofitInstance.getInstance(this).create(APIServices::class.java)

            if (mode == "edit" && depositData != null) {
                //  Gantilah sesuai endpoint edit deposit kamu
                apiServices.updateDeposit(
                    depositData.id,
                    dateString,
                    depositInt,
                    "Bearer $token"
                ).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@AddProgresTargetActivity, "Berhasil diubah", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@AddProgresTargetActivity, BerhasilMencatatProgresActivity::class.java))
                            finish()
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("DepoUpdateError", "Depo Update gagal dibuat: $errorBody")
                            Toast.makeText(this@AddProgresTargetActivity, "Gagal mengubah", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@AddProgresTargetActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

            } else {
                // ✅ Tambah data (logika asli)
                apiServices.createDeposit(
                    datePart, depositPart, "Bearer $token"
                ).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@AddProgresTargetActivity, "Deposit berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@AddProgresTargetActivity, BerhasilMencatatProgresActivity::class.java))
                            finish()
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("AddProgressTargetError","Deposit gagal: $errorBody")
                            Toast.makeText(this@AddProgresTargetActivity, "Gagal: $errorBody", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(this@AddProgresTargetActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

    }
    // Function untuk menapilkan Image dari server
    private fun showImage() {
        val sessionManager= SessionManager(this)
        val token = sessionManager.fetchAuthToken()

        val apiServices = RetrofitInstance.getInstance(this).create(APIServices::class.java)
        apiServices.getTarget("Bearer $token").enqueue(object : Callback<TargetResponse> {
            override fun onResponse(call: Call<TargetResponse?>, response: Response<TargetResponse?>) {
                if(response.isSuccessful) {
                    val targets = response.body()?.data

                    if(!targets.isNullOrEmpty()) {
                        val target = targets[0]
                        // Menganti Gmbar deflaut menjadi gambar yang diambil dari serve
                        val imageView = findViewById<ImageView>(R.id.textViewUrl)

                        Glide.with(this@AddProgresTargetActivity)
                            .load(target.file) // url gambar dari server
                            .placeholder(R.drawable.ic_placeholder) // gambar untuk loadi ketika gambar dari server lagi diambil
                            .error(R.drawable.ic_image_errror)
                            .into(imageView)

                    } else{
                        Toast.makeText(this@AddProgresTargetActivity, "Gambar Kosong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<TargetResponse?>, t: Throwable) {
                Log.e("AddProgresTargetActivity", "Error: ${t.message}")
                Toast.makeText(this@AddProgresTargetActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}