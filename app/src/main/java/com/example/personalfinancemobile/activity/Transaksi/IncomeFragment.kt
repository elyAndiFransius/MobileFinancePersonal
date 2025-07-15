package com.example.personalfinancemobile.activity.Transaksi

import android.content.Intent
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
import com.example.personalfinancemobile.app.ui.adapter.TransactionAdapter
import com.example.personalfinancemobile.app.data.model.TransactionModel

class IncomeFragment : Fragment() {
    private lateinit  var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_income, container, false)

         recyclerView = view.findViewById(R.id.recyclerViewIncome)
         recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Filter data Jenis, jadi data yang di tampilkan hanya Pemasukkan saja
        val incomeList = allTransaction.filter { it.jenis == "pemasukkan" }


        adapter = TransactionAdapter(incomeList, isIncome = true, object : TransactionAdapter.onTransactionActionListener{
            override fun onEdit(transaction: TransactionModel) {
                val intent =  Intent(requireContext(),  EditTransactionActivity::class.java)
                intent.putExtra("transaction", transaction)
                startActivity(intent)
                Log.d("IncomeFragment", "Edit transaksi: ${transaction.descripsi}")
            }

            override fun onDelete(transaction: TransactionModel) {
                // Tindakan saat user klik "Hapus"
                Log.d("IncomeFragment", "Hapus transaksi: ${transaction.descripsi}")
                // TODO: Tampilkan dialog konfirmasi hapus
            }
        })

        recyclerView.adapter = adapter

        Log.d("IncomeFragment", "Menampilkan : ${incomeList.size} Transaction pemasukkan")


        return view
    }


}