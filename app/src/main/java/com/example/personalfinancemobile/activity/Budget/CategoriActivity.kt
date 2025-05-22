package com.example.personalfinancemobile.activity.Budget

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.Priode
import com.example.personalfinancemobile.app.ui.adapter.Category
import com.example.personalfinancemobile.app.ui.adapter.CategoryAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personalfinancemobile.app.data.model.Budget
import java.util.Locale


class CategoriActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter

    // Ini adalah daftar kategori anggaran yang akan ditampilkan
    private val categories = mutableListOf(
        Category(1,"Transport", R.drawable.ic_tranport),
        Category(2,"Food", R.drawable.ic_food),
        Category(3,"Entertain", R.drawable.ic_entrain),
        Category(4,"Clothes", R.drawable.ic_clothes),
        Category(5,"Savings", R.drawable.ic_saving),
        Category(6,"Miscellaneous", R.drawable.ic_miscellaneous),
    )
    private var pemasukkan: Int = 0
    private var priode: Priode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_categori)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pemasukkan = intent.getIntExtra("pemasukkan", 0)
        priode = intent.getSerializableExtra("priode") as? Priode


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CategoryAdapter(categories)
        recyclerView.adapter = adapter

        val btnSave = findViewById<Button>(R.id.id_btnSave)

        btnSave.setOnClickListener {
            val selected = adapter.getSelectedCategories()

            if (selected.isNotEmpty()) {
                showSelectionCategories(selected)
            }
        }
    }
    private fun showSelectionCategories (selectedCategories: List<Category>) {
        // Ambil tampilan dari layout dialog
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_selected_categories,  null)

        // Buat dialog Pop Up
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val container = dialogView.findViewById<LinearLayout>(R.id.categoryContainer)

        // Menambakan setiap kategory yang di pilih untuk di tampilkan variabel dialogView
        selectedCategories.forEach { category ->
            val itemView = LayoutInflater.from(this)
                .inflate(R.layout.item_kategori, null)

            // Untuk menambahkan jarak item
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            params.bottomMargin = 20
            itemView.layoutParams = params

            val categoryName =itemView.findViewById<TextView>(R.id.txtCategoryName)
            val gambar = itemView.findViewById<ImageView>(R.id.imgCategory)
            val btnAdd = itemView.findViewById<ImageView>(R.id.id_add)

            categoryName.text = category.name
            gambar.setImageResource(category.image)
            btnAdd.visibility = View.GONE

            // Untuk menampilkan list category di dalam container dialogView
            container.addView(itemView)
        }
        dialogView.findViewById<Button>(R.id.btnNext).setOnClickListener{
            val intent = Intent(this, TotalCategoryActivity::class.java)
            intent.putExtra("pemasukkan", pemasukkan)
            intent.putExtra("priode", priode)
            intent.putExtra("Select_category", selectedCategories.toTypedArray())
            startActivity(intent)
            dialog.dismiss()
        }

        // Tampilkan dialog kedalam layar
        dialog.show()

        // Hapus background putih di luar sudut layout
        dialog.window?.setBackgroundDrawable(
            android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT)
        )

    }

}



