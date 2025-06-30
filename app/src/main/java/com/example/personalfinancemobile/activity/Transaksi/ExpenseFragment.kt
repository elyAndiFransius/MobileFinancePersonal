package com.example.personalfinancemobile.activity.Transaksi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Transaksi.MainActivity.Companion.allTransaction
import com.example.personalfinancemobile.app.data.model.TransactionModel
import com.example.personalfinancemobile.app.ui.adapter.TransactionAdapter


class ExpenseFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewExpense)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Filter data Jenis, jadi data yang di tampilkan hanya penggeluaran saja
        val expenseList = allTransaction.filter { it.jenis == "pengeluaran" }


        adapter = TransactionAdapter(expenseList, isIncome = false)
        recyclerView.adapter = adapter

        Log.d("ExpenseFragment", "Menampilkan : ${expenseList.size} Transaction pengeluran")

        return view
    }
}