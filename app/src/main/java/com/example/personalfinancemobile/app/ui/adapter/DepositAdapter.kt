package com.example.personalfinancemobile.app.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.ServerDeposit

class DepositAdapter (
        private val items: List<ServerDeposit>,
        private val listener: onDepositActionListener

        ) : RecyclerView.Adapter<DepositAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
    }

    interface onDepositActionListener {
        fun onEdit(deposit: ServerDeposit)
        fun onDelete(deposit: ServerDeposit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deposit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val item = items[position]
        Log.e("DepositAdapter", "Item deposite: ${item.date}, ${item.deposit}")
        holder.tvDate.text = item.date
        holder.tvAmount.text = item.deposit.toString()

        holder.itemView.setOnClickListener {
            val contex = holder.itemView.context
            val dialogView = LayoutInflater.from(contex).inflate(R.layout.dialog_deposit_options, null)
            val dialog = androidx.appcompat.app.AlertDialog.Builder(contex).setView(dialogView).create()

            dialogView.findViewById<TextView>(R.id.btnEdit)?.setOnClickListener {
                listener.onEdit(item)
                dialog.dismiss()
            }

            dialogView.findViewById<TextView>(R.id.btnDelete)?.setOnClickListener {
                listener.onDelete(item)
                dialog.dismiss()
            }

            dialog.show()
        }
        Log.d("DepositAdapter", "Bind item: ${item.date}, ${item.deposit}")
    }

    override fun getItemCount(): Int = items.size
}