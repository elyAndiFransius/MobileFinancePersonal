package com.example.personalfinancemobile.app.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.BudgetRequest
import com.example.personalfinancemobile.app.data.model.CategoryRequest

class BudgetAdapter (
    private val items: List<BudgetRequest>)  : RecyclerView.Adapter<BudgetAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
                val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)

            }
            override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_budget, parent, false)
                return ViewHolder(view)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val item = items[position]
                Log.d("BudgetAdapter", "Menampilkan item: ${item.priode} - ${item.pemasukkan} - ${item.categories}")
                holder.tvCategory.text = item.priode
                holder.tvAmount.text = " ${item.pemasukkan}"
            }

    override fun getItemCount(): Int {
        Log.d("BudgetAdapter", "Jumlah item: ${items.size}")
        return items.size
    }

}