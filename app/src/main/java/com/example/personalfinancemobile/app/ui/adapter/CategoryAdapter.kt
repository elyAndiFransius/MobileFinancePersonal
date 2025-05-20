package com.example.personalfinancemobile.app.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.ui.adapter.Category

// Ini class adapter untuk RecyclerView
// Adapter ini digunakan untuk menampilkan daftar kategori
class CategoryAdapter(private val categories: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    // Ini untuk menyimpan item mana saja yang sedang dipilih user
    private var selectedPosition = mutableSetOf<Int>()

    // ViewHolder = tempat menampung komponen dari 1 item
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtCategoryName) // Nama kategori
        val imageView: ImageView = itemView.findViewById(R.id.imgCategory) // Gambar kategori
        val container: View = itemView.findViewById(R.id.layoutKategori)    // Layout luar, untuk ganti background
    }

    // Membuat tampilan untuk setiap item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kategori, parent, false)
        return CategoryViewHolder(view) // Mengembalikan tampilan item yang sudah dibuat
    }

    // Mengisi data ke tampilan item
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position] // Ambil data berdasarkan posisi
        holder.txtName.text = category.name // Set nama kategori
        holder.imageView.setImageResource(category.image) // Set gambar kategori

        // Jika item ini sedang dipilih, ganti background-nya
        if (selectedPosition.contains(position)) {
            holder.container.setBackgroundResource(R.drawable.input_ripple_white) // Jika dipilih, background putih
        } else {
            holder.container.setBackgroundResource(R.drawable.input_ripple_primary) // Jika tidak, background biru
        }

        // Saat item diklik
        holder.itemView.setOnClickListener {
            if (selectedPosition.contains(position)) {
                selectedPosition.remove(position) // Kalau sebelumnya dipilih, hapus dari daftar pilihan
            } else {
                selectedPosition.add(position) // Kalau belum dipilih, tambahkan ke daftar pilihan
            }

            notifyItemChanged(position) // Refresh tampilan item ini agar background-nya ikut berubah
        }
    }

    // Mengembalikan jumlah item yang akan ditampilkan
    override fun getItemCount(): Int = categories.size

    // Mengambil semua kategori yang sedang dipilih user
    fun getSelectedCategories(): List<Category> {
        return selectedPosition.map { categories[it] } // Ambil data dari posisi yang disimpan
    }
}
