package com.example.personalfinancemobile.app.ui.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.personalfinancemobile.R
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancemobile.app.data.model.TransactionModel

class TransactionAdapter (
        private val items: List<TransactionModel>,
        private val isIncome: Boolean) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val item = items[position]
        Log.d("TransactionAdapter", "Menampilkan item: ${item.descripsi} - ${item.jumlah}")
        holder.tvDate.text = item.date
        holder.tvDescription.text = item.descripsi
        holder.tvAmount.text = if (isIncome) {
            "+ Rp ${item.jumlah}"
        }else{
            "- Rp ${item.jumlah}"
        }

        holder.tvAmount.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (isIncome) R.color.blue else R.color.red
            )
        )
        Log.d("TransactionAdapter", "Bind item: ${item.descripsi} - ${item.jumlah}")

    }

    override fun getItemCount(): Int = items.size


}