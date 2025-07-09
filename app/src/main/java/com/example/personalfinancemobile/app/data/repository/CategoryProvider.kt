package com.example.personalfinancemobile.app.data.repository
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.ui.adapter.Category

object CategoryProvider {
    fun getDefaultCategories(): List<Category> {
        return listOf(
            Category(1, "Transportasi", R.drawable.ic_tranport),
            Category(2, "Makanan dan minuman", R.drawable.ic_food),
            Category(3, "Pendidikan", R.drawable.ic_pendidikan),
            Category(4, "Hiburan", R.drawable.ic_entrain),
            Category(5, "Pakaian", R.drawable.ic_clothes),
            Category(6, "Tabungan", R.drawable.ic_saving)
        )
    }
    fun getRecomendedAllocation (): Map<String, Int> {
        return mapOf(
            "Makan & minum" to 35,
            "Transportasi" to 25,
            "Pendidikan" to 10,
            "Hiburan" to 5,
            "Pakaian" to 5,
            "Tabungan" to 5

        )
    }


}
