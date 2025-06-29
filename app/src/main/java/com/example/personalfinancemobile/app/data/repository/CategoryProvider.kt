package com.example.personalfinancemobile.app.data.repository
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.ui.adapter.Category

object CategoryProvider {
    fun getDefaultCategories(): List<Category> {
        return listOf(
            Category(1, "Transport", R.drawable.ic_tranport),
            Category(2, "Food", R.drawable.ic_food),
            Category(3, "Entertain", R.drawable.ic_entrain),
            Category(4, "Clothes", R.drawable.ic_clothes),
            Category(5, "Savings", R.drawable.ic_saving),
            Category(6, "Miscellaneous", R.drawable.ic_miscellaneous)
        )
    }
}