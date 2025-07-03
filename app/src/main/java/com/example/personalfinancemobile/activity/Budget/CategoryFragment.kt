package com.example.personalfinancemobile.activity.Budget

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Budget.ListBudgetActivity.Companion.allBudget
import com.example.personalfinancemobile.activity.Transaksi.MainActivity.Companion.allTransaction
import com.example.personalfinancemobile.app.data.model.CategoryRequest
import com.example.personalfinancemobile.app.ui.adapter.BudgetAdapter
import com.example.personalfinancemobile.app.ui.adapter.CategoryAdapter
import com.example.personalfinancemobile.app.ui.adapter.CategoryDisplayAdapter


class CategoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryDisplayAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewCategory)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        // Ambil data dari arguments
        val categoryList = arguments?.getParcelableArrayList<CategoryRequest>("categories") ?: emptyList()
        adapter = CategoryDisplayAdapter(categoryList)
        Log.d("CategoryFragment", "Jumlah kategori dari arguments: ${categoryList.size}")

        if (categoryList.isEmpty()) {
            Log.w("CategoryFragment", "Tidak ada kategori ditemukan")
            // Tambahkan TextView di layout dan tampilkan pesan "Data kosong" jika perlu
        }
        recyclerView.adapter = adapter

        return view
    }
    companion object {
        @JvmStatic
        fun newInstance(data: List<CategoryRequest>): CategoryFragment {
            val fragment = CategoryFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("categories", ArrayList(data)) // harus Parcelable
            fragment.arguments = bundle
            return fragment
        }
    }

}