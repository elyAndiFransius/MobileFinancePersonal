package com.example.personalfinancemobile.activity.Deposit

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
import com.example.personalfinancemobile.activity.Budget.BudgedSchedulingActivity
import com.example.personalfinancemobile.activity.Budget.ListBudgetActivity
import com.example.personalfinancemobile.activity.Deposit.DepoMainActivity.Companion.allDeposit
import com.example.personalfinancemobile.activity.target.AddProgresTargetActivity
import com.example.personalfinancemobile.app.data.model.ServerDeposit
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.ui.adapter.DepositAdapter
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DepositFragment : Fragment() {
    private lateinit var adapter: DepositAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_deposit, container, false)


        recyclerView = view.findViewById(R.id.recyclerViewDeposit)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val allDeposit = allDeposit

        adapter = DepositAdapter(allDeposit, object : DepositAdapter.onDepositActionListener{
            override fun onEdit(deposit: ServerDeposit) {
                val intent= Intent(requireContext(), AddProgresTargetActivity::class.java)
                intent.putExtra("mode", "edit")
                intent.putExtra("deposit", deposit)
                startActivity(intent)
                Log.d("DepositFragment", "Edit Deposit: ${deposit.date}, ${deposit.deposit}")
            }

            override fun onDelete(deposit: ServerDeposit) {
                val sessionManager = SessionManager(requireContext())
                val token = sessionManager.fetchAuthToken()

                val apiServies = RetrofitInstance.getInstance(requireContext()).create(APIServices::class.java)
                apiServies.deletedepo(deposit.id ,"Bearer $token").enqueue(object : Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Data berhasil di hapus", Toast.LENGTH_SHORT).show()
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("deleteError", "Gagal mengahapus data deposit: $errorBody")
                            Toast.makeText(requireContext(), "Gagal menghapus deposit: $errorBody",
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

        return view
    }

}