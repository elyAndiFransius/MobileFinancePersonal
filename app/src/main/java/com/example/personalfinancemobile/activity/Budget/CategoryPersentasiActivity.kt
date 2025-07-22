package com.example.personalfinancemobile.activity.Budget

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.Priode
import com.example.personalfinancemobile.app.data.repository.CategoryProvider
import com.example.personalfinancemobile.app.ui.adapter.Category
import com.example.personalfinancemobile.app.ui.utils.setupBackButton

class CategoryPersentasiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_persentasi)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backButton = findViewById<ImageView>(R.id.id_back)
        setupBackButton(this, backButton)
        // Ambil data dari activy Category
        val pemasukkan = intent.getIntExtra("pemasukkan", 0)
        val priode = intent.getSerializableExtra("priode") as? Priode

        // Untuk menampilkan kalimat pedukung untuk persentation pengeluaran
        val textView2 = findViewById<TextView>(R.id.textView2)
        priode?.let {
            val pemasulanJumalah = "Rp %,d".format(pemasukkan).replace(',', '.')
            val text = "Ini adalah tampilan yang relevan dari anggaran<br> he <b>${it.name}</b> dengan jumlah sebesar<br> to <b>$pemasulanJumalah</b>."
            textView2.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        }

        // Ambil data dari activy Category
        val parcelableArray = intent.getParcelableArrayExtra("Select_category")
        val selectedCategories = parcelableArray?.filterIsInstance<Category>()
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnReset = findViewById<AppCompatButton>(R.id.btnReset)
        val recommended = CategoryProvider.getRecomendedAllocation()

        // Pisahkan kategori berdasarkan apakah list rekomendasi
        val (recommendedCategories, userDefindCategories) = selectedCategories.orEmpty().partition {
            recommended.containsKey(it.name)
        }

        // Hitung total persentasi yang sudah di pakai rekomendasi
        val usedPersen = recommendedCategories.sumOf { recommended[it.name] ?: 0}
        val remainingPersent = 100 - usedPersen

        // Hitung total persentasi yang categori di inputkan oleh user
        val additionalPercent  = if (userDefindCategories.isNotEmpty()) {
            remainingPersent / userDefindCategories.size
        } else 0

        // gabungkan kedua kateogri
        val categoryAllocation = mutableMapOf<String, Int>()

        recommendedCategories.forEach {
            categoryAllocation[it.name] = recommended[it.name] ?: 0
        }

        userDefindCategories.forEach {
            categoryAllocation[it.name] = additionalPercent
        }

        // Untuk memanggil categoryContainer untuk menampilkan Pop-up Category
        val container = findViewById<LinearLayout>(R.id.categoryContainer)
        val totalBudget = pemasukkan

        selectedCategories?.forEach { category ->
            val persen = categoryAllocation[category.name] ?: 0

            val itemView = LayoutInflater.from(this)
                .inflate(R.layout.item_kategori, null)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = 16
            itemView.layoutParams = params

            val txtName = itemView.findViewById<TextView>(R.id.txtCategoryName)
            val imgIcon = itemView.findViewById<ImageView>(R.id.imgCategory)
            val btnAdd = itemView.findViewById<ImageView>(R.id.id_add)

            txtName.text = "${category.name}  (${persen}%)"
            imgIcon.setImageResource(category.image)
            btnAdd.visibility = View.GONE

            container.addView(itemView)


            //  Untuk menggarakan dan menggirim Data kedalam activity TotalCategory
            btnSave.setOnClickListener {
                val intent = Intent(this, CategoryTotalActivity::class.java)
                intent.putExtra("Kategory", selectedCategories.toTypedArray())
                intent.putExtra("priode", priode ?: "Harian")
                intent.putExtra("jumlah",totalBudget)
                startActivity(intent)
            }
            btnReset.setOnClickListener {
                val intent = Intent(this, CategoryResetActivity::class.java)
                intent.putExtra("Kategory", selectedCategories.toTypedArray())
                intent.putExtra("priode", priode ?: "Harian")
                intent.putExtra("jumlah",totalBudget)
                startActivity(intent)
            }
        }
    }
}
