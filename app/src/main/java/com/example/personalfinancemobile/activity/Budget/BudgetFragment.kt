package com.example.personalfinancemobile.activity.Budget


import com.example.personalfinancemobile.app.ui.adapter.CategoryAdapter
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.ui.adapter.Category
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.ArrayList

// Ini adalah tampilan layar (fragment) untuk fitur Budget (anggaran)
class BudgetFragment : Fragment() {
//
//    // Komponen UI
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var fab: FloatingActionButton // belum dipakai di kode ini
//    private lateinit var adapter: CategoryAdapter
//
//    // Ini adalah daftar kategori anggaran yang akan ditampilkan
//    private val categories = mutableListOf(
//        Category("Transport", R.drawable.ic_tranport),
//        Category("Food", R.drawable.ic_food),
//        Category("Entertain", R.drawable.ic_entrain),
//        Category("Clothes", R.drawable.ic_clothes),
//        Category("Savings", R.drawable.ic_saving),
//        Category("Miscellaneous", R.drawable.ic_miscellaneous),
//    )
//
//
//    // Fungsi ini dijalankan saat fragment ditampilkan
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Menghubungkan layout XML dengan fragment ini
//        val view = inflater.inflate(R.layout.fragment_budget, container, false)
//
//        // Mengatur RecyclerView supaya bisa menampilkan daftar kategori
//        recyclerView = view.findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // agar tampil dari atas ke bawah
//        adapter = CategoryAdapter(categories) // buat adapter pakai data kategori
//        recyclerView.adapter = adapter // sambungkan adapter ke RecyclerView
//
//        // Saat tombol Simpan ditekan
//        val btnSave = view.findViewById<Button>(R.id.btnSave)
//        btnSave.setOnClickListener {
//            val selected = adapter.getSelectedCategories() // ambil kategori yang dipilih user
//
//            if (selected.isNotEmpty()) {
//                showSelectedCategories(selected) // tampilkan dialog daftar yang dipilih
//            }
//        }
//
//        return view // kembalikan tampilan
//    }
//
//    // Fungsi ini akan menampilkan dialog berisi kategori yang dipilih user
//    private fun showSelectedCategories (selectedCategories: List<Category>) {
//        // Ambil tampilan dari layout dialog
//        val dialogView = LayoutInflater.from(requireContext())
//            .inflate(R.layout.dialog_selected_categories, null)
//
//
//        // Buat dialog pop-up
//        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
//            .setView(dialogView)
//            .create()
//
//        // Container di dalam dialog tempat untuk menambahkan item yang dipilih
//        val container = dialogView.findViewById<LinearLayout>(R.id.categoryContainer)
//
//
//        // Tambahkan setiap kategori yang dipilih ke dalam dialog
//        selectedCategories.forEach { category ->
//            val itemView = LayoutInflater.from(requireContext())
//                .inflate(R.layout.item_kategori, null)
//
//            // Tambahkan jarak antar item
//            val params = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            params.bottomMargin = 20
//            itemView.layoutParams = params
//
//            val txtName = itemView.findViewById<TextView>(R.id.txtCategoryName)
//            val imgIcon = itemView.findViewById<ImageView>(R.id.imgCategory)
//            val btnAdd = itemView.findViewById<ImageView>(R.id.id_add)
//
//
//            txtName.text = category.name
//            imgIcon.setImageResource(category.image)
//            btnAdd.visibility = View.GONE
//
//            container.addView(itemView)
//        }
//
//        // Kalau tombol "Next" di dalam dialog diklik, maka tutup dialog
//        dialogView.findViewById<Button>(R.id.btnNext).setOnClickListener {
//            val intent = Intent(requireContext(), TotalCategoryActivity::class.java)
//            intent.putExtra("selected_categories", selectedCategories.toTypedArray())
//            startActivity(intent)
//            dialog.dismiss()
//        }
//
//        // Tampilkan dialog ke layar
//        dialog.show()
//
//        // Hapus background putih di luar sudut layout
//        dialog.window?.setBackgroundDrawable(
//            android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT)
//        )
//    }
}
