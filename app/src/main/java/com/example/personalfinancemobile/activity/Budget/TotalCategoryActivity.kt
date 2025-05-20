package com.example.personalfinancemobile.activity.Budget

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.ui.adapter.Category

class TotalCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_total_category)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val parcelableArray = intent.getParcelableArrayExtra("selected_categories")
        val selectedCategories = parcelableArray?.filterIsInstance<Category>()
//        val btnNext = findViewById<Button>(R.id.btnNext)


        var categoryAllocation = mapOf(
            "Transport" to 20,
            "Food" to 30,
            "Entertain" to 30,
            "Clothes" to 10,
            "Savings" to 5,
            "Miscellaneous" to 5
        )

        val container = findViewById<LinearLayout>(R.id.categoryContainer)

        val totalBudget = 1_000_000

        selectedCategories?.forEach { category ->
            val persen = categoryAllocation[category.name] ?: 0
            val allocation = totalBudget * persen / 100

            val itemView = LayoutInflater.from(this)
                .inflate(R.layout.item_kategori, null)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = 16
            itemView.layoutParams = params

            val txtName = itemView.findViewById<TextView>(R.id.txtCategoryName)
            val imgIcon = itemView.findViewById<ImageView>(R.id.imgCategory)


            txtName.text = "${category.name} - Rp $allocation (${persen}%)"
            imgIcon.setImageResource(category.image)

            container.addView(itemView)
        }
            //  Untuk mengarakan kedalam activity TotalCategory
//            btnNext.setOnClickListener{
//                val intent = Intent(this, TotalCategoryPersenActivity::class.java)
//                startActivity(intent)
//        }
    }
}
