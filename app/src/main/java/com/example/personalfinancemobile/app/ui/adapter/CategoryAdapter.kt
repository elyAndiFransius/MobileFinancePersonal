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


class CategoryAdapter(private val categories: List<Category>, private val onAddCategoryAdapter: () ->Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    // Ini untuk menyimpan item mana saja yang sedang dipilih user
    private var selectedPosition = mutableSetOf<Int>()


    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtCategoryName) // Nama kategori
        val imageView: ImageView = itemView.findViewById(R.id.imgCategory) // Gambar kategori
        val container: View = itemView.findViewById(R.id.layoutKategoriView)    // Layout luar, untuk ganti background
    }

    // Membuat tampilan untuk setiap item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kategori, parent, false)
        return CategoryViewHolder(view) // Mengembalikan tampilan item yang sudah dibuat
    }

    // Mengisi data ke tampilan item
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.txtName.text = category.name
        holder.imageView.setImageResource(category.image)

        if (category.id == -1) {
            holder.container.setBackgroundResource(R.drawable.input_ripple_primary)
            holder.itemView.setOnClickListener {
                onAddCategoryAdapter()
            }
            return
        }

        if (selectedPosition.contains(position)) {
            holder.container.setBackgroundResource(R.drawable.input_ripple_white) // Jika dipilih, background putih
        }else {
            holder.container.setBackgroundResource(R.drawable.input_ripple_primary) // Jika tidak, background biru
        }

        // Saat item diklik
        holder.itemView.setOnClickListener {
            if (selectedPosition.contains(position)) {
                selectedPosition.remove(position)
            } else {
                selectedPosition.add(position)
            }
            notifyItemChanged(position)
        }
    }

    // Mengembalikan jumlah item yang akan ditampilkan
    override fun getItemCount(): Int = categories.size

    // Mengambil semua kategori yang sedang dipilih user
    fun getSelectedCategories(): List<Category> {
        return selectedPosition.map { categories[it] } // Ambil data dari posisi yang disimpan
    }

    fun selectedPosition(position: Int) {
        selectedPosition.add(position)
        notifyItemChanged(position)
    }


}
