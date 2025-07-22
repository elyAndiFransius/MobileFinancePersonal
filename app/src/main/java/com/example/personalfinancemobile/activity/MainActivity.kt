package com.example.personalfinancemobile.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Auth.LoginActivity
import com.example.personalfinancemobile.activity.Transaksi.MainActivity as Transaksi
import com.example.personalfinancemobile.activity.Budget.MainBudgetingActivity
import com.example.personalfinancemobile.activity.target.AddProgresTargetActivity
import com.github.mikephil.charting.data.Entry
import com.example.personalfinancemobile.activity.target.MainTargetActivity
import com.example.personalfinancemobile.app.data.model.Auth.GrafikCategori
import com.example.personalfinancemobile.app.data.model.Auth.GrafikTarget
import com.example.personalfinancemobile.app.data.model.BudgetingResponse
import com.example.personalfinancemobile.app.data.model.TargetResponse
import com.example.personalfinancemobile.app.data.model.UserResponseObject
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.data.repository.CategoryProvider
import com.example.personalfinancemobile.app.ui.utils.NumberFormatText
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF


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

        val kategoriContainer = findViewById<LinearLayout>(R.id.kategori)
        val categories = CategoryProvider.getDefaultCategories()

        refreshData()

        for (category in categories) {
            val itemLayout = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 0, 8, 0)
                }
                orientation = LinearLayout.HORIZONTAL
                setPadding(16, 8, 16, 8)
                background = ContextCompat.getDrawable(this@MainActivity, R.drawable.kategori_background)
                gravity = Gravity.CENTER_VERTICAL
            }

            val icon = ImageView(this).apply {
                setImageResource(category.image)
                layoutParams = LinearLayout.LayoutParams(60, 60)
            }

            val text = TextView(this).apply {
                text = category.name.split(" ").first() // Ambil kata pertama saja (optional)
                setTextColor(Color.BLACK)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                setPadding(8, 0, 0, 0)
            }

            itemLayout.addView(icon)
            itemLayout.addView(text)
            kategoriContainer.addView(itemLayout)
        }


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
    private fun refreshData() {
        getUser()
        Buget()
        grafikTarget()
        grafikCategori()
        Notifikasi()
        grafikCategori()
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
                        balance.text = NumberFormatText(budget.pemasukkan.toLong())
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

    private fun grafikCategori() {
        val pieChart = findViewById<PieChart>(R.id.pieChart)
        val emptyLayout = findViewById<LinearLayout>(R.id.emptyBudgetLayout)
        val jumlah = findViewById<TextView>(R.id.tvHarga)
        val priode = findViewById<TextView>(R.id.tvPeriode)
        val btnTransc = findViewById<AppCompatButton>(R.id.btnTransc)
        val tvPembatas = findViewById<TextView>(R.id.tvPembatas)
        val garis = findViewById<View>(R.id.garis)

        val sessionManager = SessionManager(this)
        val token = sessionManager.fetchAuthToken()
        val apiService = RetrofitInstance.getInstance(this).create(APIServices::class.java)

        apiService.getKategoriData("Bearer $token").enqueue(object : Callback<List<GrafikCategori>> {
            override fun onResponse(
                call: Call<List<GrafikCategori>>,
                response: Response<List<GrafikCategori>>
            ) {
                if (response.isSuccessful) {
                    val kategoriList = response.body() ?: emptyList()

                    if (kategoriList.isNotEmpty()) {
                        val apiBudget = kategoriList[0]
                        jumlah.text = NumberFormatText(apiBudget.pemasukkan.toLong())
                        priode.text = apiBudget.priode.toString()

                        Log.d("BUDGET", "Menampilkan grafik")

                        // Tampilkan elemen
                        emptyLayout.visibility = View.GONE
                        jumlah.visibility = View.VISIBLE
                        priode.visibility = View.VISIBLE
                        btnTransc.visibility = View.VISIBLE
                        tvPembatas.visibility = View.VISIBLE
                        garis.visibility = View.VISIBLE
                        pieChart.visibility = View.VISIBLE

                        showPieChart(pieChart, kategoriList)
                    } else {
                        Log.d("BUDGET", "Data kosong")

                        // Kosongkan pieChart jika data kosong
                        pieChart.clear()
                        pieChart.invalidate()

                        // Sembunyikan elemen dan tampilkan layout kosong
                        emptyLayout.visibility = View.VISIBLE
                        jumlah.visibility = View.GONE
                        priode.visibility = View.GONE
                        btnTransc.visibility = View.GONE
                        tvPembatas.visibility = View.GONE
                        garis.visibility = View.GONE
                        pieChart.visibility = View.GONE
                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("API_ERROR", "Gagal ambil data budget: $errorBody")
                    Toast.makeText(this@MainActivity, "Gagal ambil data budget $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GrafikCategori>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("Fail", "coba lagi: ${t.message}")
            }
        })

        // Button listener tetap aktif meskipun sebelum data datang
        btnTransc.setOnClickListener {
            val intent = Intent(this@MainActivity, Transaksi::class.java)
            startActivity(intent)
        }
    }
    private fun showPieChart(pieChart: PieChart, data: List<GrafikCategori>) {
        val entries = ArrayList<PieEntry>()
        val totalPemasukkan = data.firstOrNull()?.pemasukkan ?: 0

        // Ambil daftar kategori beserta icon dari CategoryProvider
        val iconMap = CategoryProvider.getDefaultCategories().associateBy { it.name.lowercase() }

        data.forEach {
            val originalDrawable = iconMap[it.kategori.lowercase()]?.let { category ->
                ContextCompat.getDrawable(this, category.image)
            } ?: ContextCompat.getDrawable(this, R.drawable.ic_miscellaneous)

            // Resize icon (misal: 48x48 px)
            originalDrawable?.setBounds(0, 0, 5, 5)

            val entry = PieEntry(it.jumlah.toFloat(), it.kategori, originalDrawable)
            entries.add(entry)
        }


        val dataSet = PieDataSet(entries, "")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 12f

        // Aktifkan icon
        dataSet.setDrawIcons(true)
        dataSet.iconsOffset = MPPointF(0f, 0f)


        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter(pieChart))

        pieChart.data = pieData
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.centerText = "Total:\nRp$totalPemasukkan"
        pieChart.setCenterTextSize(16f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.animateY(1000)
        pieChart.legend.isEnabled = false // sembunyikan legenda
        pieChart.invalidate()

        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val pieEntry = e as? PieEntry
                pieEntry?.let {
                    val kategori = it.label
                    val jumlah = it.value.toInt()
                    Toast.makeText(this@MainActivity, "$kategori: Rp$jumlah", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected() {}
        })
    }
    private fun grafikTarget() {
        val sessionManager = SessionManager(this)
        val token = sessionManager.fetchAuthToken()
        val apiService = RetrofitInstance.getInstance(this).create(APIServices::class.java)

        val pieChart = findViewById<PieChart>(R.id.pieTarget)
        val emptyLayout = findViewById<LinearLayout>(R.id.emptyTargetLayout)
        val priode = findViewById<TextView>(R.id.tvPeriode1)
        val tvJumlah = findViewById<TextView>(R.id.tvJumlah)
        val btnDeposit = findViewById<AppCompatButton>(R.id.btnDepsit)
        val tvPembatas = findViewById<TextView>(R.id.tvPembatas1)
        val garis = findViewById<View>(R.id.view2)

        btnDeposit.setOnClickListener {
            val intent = Intent(this@MainActivity, MainTargetActivity::class.java)
            startActivity(intent)
        }

        apiService.getTargetData("Bearer $token").enqueue(object : Callback<GrafikTarget> {
            override fun onResponse(call: Call<GrafikTarget>, response: Response<GrafikTarget>) {
                if (response.isSuccessful) {
                    val targetData = response.body()

                    if (targetData == null || targetData.targetAmount == 0) {
                        Log.d("TARGET", "Data kosong atau target = 0")
                        emptyLayout.visibility = View.VISIBLE
                        priode.visibility = View.GONE
                        tvJumlah.visibility = View.GONE
                        pieChart.visibility = View.GONE
                        btnDeposit.visibility = View.GONE
                        tvPembatas.visibility = View.GONE
                        garis.visibility = View.GONE

                        // Tambahkan ini agar PieChart dibersihkan
                        pieChart.clear()
                        pieChart.invalidate()
                    } else {
                        Log.d("TARGET", "Menampilkan grafik")

                        emptyLayout.visibility = View.GONE
                        tvPembatas.visibility = View.VISIBLE
                        tvJumlah.visibility = View.VISIBLE
                        pieChart.visibility = View.VISIBLE
                        garis.visibility = View.VISIBLE
                        priode.visibility = View.VISIBLE
                        btnDeposit.visibility = View.VISIBLE

                        // Menampilkan jumlah target dengan format rupiah
                        tvJumlah.text = "Rp ${NumberFormatText(targetData.targetAmount.toLong())}"

                        // Tambahkan ini agar PieChart dibersihkan
                        pieChart.clear()
                        pieChart.invalidate()
                        // Tampilkan chart
                        showPieChartTarget(pieChart, targetData)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("API_ERROR", "Gagal ambil data: $errorBody")
                    Toast.makeText(this@MainActivity, "Gagal ambil data target", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GrafikTarget>, t: Throwable) {
                Log.e("API_FAIL", "Network error: ${t.message}")
                Toast.makeText(this@MainActivity, "Error jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showPieChartTarget(pieChart: PieChart, data: GrafikTarget) {
        val progress = data.currentAmount.toFloat()
        val target = data.targetAmount.toFloat()
        val remaining = target - progress

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(progress, "Terkumpul"))
        entries.add(PieEntry(remaining, "Sisa"))

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(Color.parseColor("#4CAF50"), Color.LTGRAY)
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 12f

        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter(pieChart))

        pieChart.data = pieData
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.centerText = "${data.gol}\n${progress.toInt()}"
        pieChart.setCenterTextSize(16f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.animateY(1000)
        pieChart.legend.isEnabled = false
        pieChart.invalidate()


    }



}