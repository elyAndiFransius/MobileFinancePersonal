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
import com.example.personalfinancemobile.app.ui.adapter.TransactionAdapter
import com.example.personalfinancemobile.app.data.model.TransactionModel

class IncomeFragment : Fragment() {
    private lateinit  var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private lateinit var transactionList: List<TransactionModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_income, container, false)

         recyclerView = view.findViewById(R.id.recyclerViewIncome)
         recyclerView.layoutManager = LinearLayoutManager(requireContext())

        transactionList = listOf(
            TransactionModel("12 Feb 2025", "Tranding", 500000),
            TransactionModel("13 Feb 2025", "Bulanan", 6000000)
        )
        adapter = TransactionAdapter(transactionList, isIncome = true)
        recyclerView.adapter = adapter

        Log.d("IncomeFragment", "onCreateView: Memuat data Income")
        Log.d("IncomeFragment", "Jumlah item: ${transactionList.size}")


        return view
    }


}