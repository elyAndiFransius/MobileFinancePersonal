package com.example.personalfinancemobile.activity.Transaksi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Transaksi.MainActivity.Companion.allTransaction
import com.example.personalfinancemobile.app.ui.adapter.TransactionAdapter
import com.example.personalfinancemobile.app.data.model.TransactionModel
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                val intent =  Intent(requireContext(),  AddTransactionActivity::class.java)
                intent.putExtra("mode", "edit")
                intent.putExtra("transaction", transaction)
                startActivity(intent)
                Log.d("IncomeFragment", "Edit transaksi: ${transaction.descripsi}")
            }

            override fun onDelete(transaction: TransactionModel) {
                val sessionManager = SessionManager(requireContext())
                val token = sessionManager.fetchAuthToken()

                val apiServies = RetrofitInstance.getInstance(requireContext()).create(APIServices::class.java)
                apiServies.deleteTransaction(transaction.id ,"Bearer $token").enqueue(object : Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Data berhasil di hapus", Toast.LENGTH_SHORT).show()
                            Log.d("IncomeExpenseFragment", "Hapus transaksi: ${transaction.descripsi}")
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("deleteError", "Gagal mengahapus data transaction: $errorBody")
                            Toast.makeText(requireContext(), "Gagal menghapus transaction: $errorBody",
                                Toast.LENGTH_SHORT ).show()
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("DeleteError", "Throwable: ${t.message}")
                    }
                })
            }
        })

        recyclerView.adapter = adapter

        Log.d("IncomeFragment", "Menampilkan : ${incomeList.size} Transaction pemasukkan")


        return view
    }


}