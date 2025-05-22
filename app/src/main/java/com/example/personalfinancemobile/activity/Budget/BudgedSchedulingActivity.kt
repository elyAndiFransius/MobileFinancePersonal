package com.example.personalfinancemobile.activity.Budget

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.Budget
import com.example.personalfinancemobile.app.data.model.Category
import com.example.personalfinancemobile.app.data.model.Priode

class BudgedSchedulingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budged_scheduling)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnNext = findViewById<AppCompatButton>(R.id.id_btn_Set)
        val jumlah = findViewById<EditText>(R.id.id_belance)
        val priodeOptions = listOf("Harian", "Mingguan", "Bulanan", "Tahunan", "Custom")
        val autoComplate = findViewById<AutoCompleteTextView>(R.id.id_priode)

        btnNext.setOnClickListener {
            val pemasukkanStr = jumlah.text.toString()
            val priodeStr = autoComplate.text.toString()

            if (pemasukkanStr.isNotEmpty() && priodeStr.isNotEmpty()) {
                val pemasukkan = pemasukkanStr.toInt()

                val priode = when (priodeStr) {
                    "Harian" -> Priode.HARIAN
                    "Mingguan" -> Priode.MINGGUAN
                    "Bulanan" -> Priode.BULANAN
                    "Tahunan" -> Priode.TAHUNAN
                    "Custom" -> Priode.CUSTOM
                    else -> Priode.BULANAN  // Jika tidak di antara di atas maka akan di kembalikan ke default Bulanan
                }
                val intent = Intent(this, CategoriActivity::class.java)
                intent.putExtra("pemasukkan", pemasukkan)
                intent.putExtra("priode", priode)
                startActivity(intent)
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, priodeOptions)
        autoComplate.setAdapter(adapter)
        //Untuk menampilkan drop-dwn priode
        autoComplate.setOnClickListener {
            autoComplate.showDropDown()
        }
        // Untuk Custom priode
        autoComplate.setOnItemClickListener { parent, view, position, id ->
            val selected = parent.getItemAtPosition(position).toString()
                if (selected == "Custom") {
                    // TODO: Tampilkan DatePicker atau dialog untuk input custom priode
                }

            }

    }
}