package com.example.personalfinancemobile.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.CategoryRequest
import com.example.personalfinancemobile.app.data.repository.CategoryProvider
import com.example.personalfinancemobile.app.ui.adapter.Category

class CategoryDisplayAdapter (
    private val category: List<CategoryRequest>) :
    RecyclerView.Adapter<CategoryDisplayAdapter.ViewHolder>() {

    // Buat map nama kategori (lowercase) ke icon resource ID
    private val categoryIconMap: Map<String, Int> = CategoryProvider.getDefaultCategories()
        .associate { it.name.lowercase() to it.image }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val icon: ImageView = itemView.findViewById(R.id.icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_budget, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = category[position]
        holder.tvCategory.text = item.name
        holder.tvAmount.text = item.jumlah.toString()

        // Ambil icon resource ID langsung dari map
        val iconResId = categoryIconMap[item.name.lowercase()] ?: R.drawable.ic_miscellaneous
        holder.icon.setImageResource(iconResId)
    }

    override fun getItemCount(): Int = category.size
    }