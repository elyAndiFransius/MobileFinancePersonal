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
import com.example.personalfinancemobile.app.data.model.TransactionModel
import com.example.personalfinancemobile.app.ui.adapter.TransactionAdapter


class ExpenseFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private lateinit var transactionList: List<TransactionModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewExpense)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        transactionList = listOf(
            TransactionModel("12 Feb 2025", "Phone Repair", -200000),
            TransactionModel("13 Feb 2025", "Belanja", -150000)
        )

        adapter = TransactionAdapter(transactionList, isIncome = false)
        recyclerView.adapter = adapter

        Log.d("ExpenseFragment", "onCreateView: Memuat data Expense")
        Log.d("ExpenseFragment", "Jumlah item: ${transactionList.size}")

        return view
    }
}