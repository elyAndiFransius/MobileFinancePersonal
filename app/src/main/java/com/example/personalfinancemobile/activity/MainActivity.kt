package com.example.personalfinancemobile.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Auth.LoginActivity
import com.example.personalfinancemobile.activity.Transaksi.MainActivity as Transaksi
import com.example.personalfinancemobile.activity.Budget.MainBudgetingActivity
import com.example.personalfinancemobile.activity.target.AddProgresTargetActivity
import com.example.personalfinancemobile.activity.target.HomeTargetActivity
import com.example.personalfinancemobile.activity.target.MainTargetActivity
import com.example.personalfinancemobile.app.data.model.BudgetingResponse
import com.example.personalfinancemobile.app.data.model.TargetResponse
import com.example.personalfinancemobile.app.data.model.UserResponseObject
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    companion object {
        const val CHANNEL_ID = "channel_id"
    }

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

        val logOut = findViewById<ImageView>(R.id.btnLogOut)

        getUser()
        Buget()
        Notifikasi()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }



        logOut.setOnClickListener {
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
    private fun Notifikasi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel Notifikasi",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel untuk notifikasi keuangan"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    // Function untuk menapilkan data pengguna dari server
    private fun Buget() {
        val sessionManager = SessionManager(this)
        val token = sessionManager.fetchAuthToken()

        val apiServices = RetrofitInstance.getInstance(this).create(APIServices::class.java)
        apiServices.budgetingIndex("Bearer $token").enqueue(object : Callback<BudgetingResponse> {
            override fun onResponse(
                call: Call<BudgetingResponse?>,
                response: Response<BudgetingResponse?>
            ) {
                if (response.isSuccessful) {
                    val budgets = response.body()?.data

                    if (!budgets.isNullOrEmpty()) {
                        val budget = budgets[0]
                        // Menganti Gmbar deflaut menjadi gambar yang diambil dari serve
                        val balance = findViewById<TextView>(R.id.balance)
                        balance.text = budget.pemasukkan.toString()
                    } else {
                        Toast.makeText(this@MainActivity, "Budget Kosong", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<BudgetingResponse?>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private  fun getUser() {
        val sessionManager= SessionManager(this)
        val token = sessionManager.fetchAuthToken()

        val apiServices = RetrofitInstance.getInstance(this).create(APIServices::class.java)
        apiServices.getUser("Bearer $token").enqueue(object : Callback<UserResponseObject> {
            override fun onResponse(call: Call<UserResponseObject?>, response: Response<UserResponseObject?>) {
                if (response.isSuccessful) {
                    val user = response.body()?.user

                    val name = findViewById<TextView>(R.id.vw_name)
                    name.text = user?.name ?: "Nama tidak ditemukan"
                    Log.d("Main", "Nama dari server: ${user?.name}")
                    Log.d("Main", "TextView ditemukan: ${name != null}")

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Main", "Gagal: $errorBody")
                    }
                }
            override fun onFailure(call: Call<UserResponseObject?>, t: Throwable) {
                Log.e("Main", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}