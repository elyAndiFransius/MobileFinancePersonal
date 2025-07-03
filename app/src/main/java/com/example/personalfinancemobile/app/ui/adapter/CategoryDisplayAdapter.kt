package com.example.personalfinancemobile.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.CategoryRequest

class CategoryDisplayAdapter (
    private val category: List<CategoryRequest>) :
    RecyclerView.Adapter<CategoryDisplayAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
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
    }

    override fun getItemCount(): Int = category.size
    }