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
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.Budget
import com.example.personalfinancemobile.app.data.model.Priode
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.ui.adapter.Category

class TotalCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_total_category)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Ambil data dari activy Category
        val pemasukkan = intent.getIntExtra("pemasukkan", 0)
        val priode = intent.getSerializableExtra("priode") as? Priode

        // Untuk menampilkan kalimat pedukung untuk persentation pengeluaran
        val textView2 = findViewById<TextView>(R.id.textView2)
        priode?.let {
            val pemasulanJumalah = "Rp %,d".format(pemasukkan).replace(',', '.')
            val text = "This is a relevant presentation of <br> he <b>${it.name}</b> budget, which amounts <br> to <b>$pemasulanJumalah</b>."
            textView2.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        }

        // Ambil data dari activy Category
        val parcelableArray = intent.getParcelableArrayExtra("Select_category")
        val selectedCategories = parcelableArray?.filterIsInstance<Category>()
        val btnNext = findViewById<Button>(R.id.btnSave)

        var categoryAllocation = mapOf(
            "Transport" to 20,
            "Food" to 30,
            "Entertain" to 30,
            "Clothes" to 10,
            "Savings" to 5,
            "Miscellaneous" to 5
        )

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

            //  Untuk mengarakan kedalam activity TotalCategory
            btnNext.setOnClickListener {
                val intent = Intent(this, TotalCategoryPersenActivity::class.java)
                intent.putExtra("Kategory", selectedCategories.toTypedArray())
                intent.putExtra("Jumlah", totalBudget)
                startActivity(intent)
            }
        }
    }
}
