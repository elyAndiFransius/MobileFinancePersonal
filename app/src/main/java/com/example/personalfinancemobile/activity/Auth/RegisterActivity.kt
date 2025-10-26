package com.example.personalfinancemobile.activity.Auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.text.InputType
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Auth.LoginActivity
import com.example.personalfinancemobile.app.data.model.User
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // 🔹 Atur warna navigation bar sesuai warna form
        val formColor = ContextCompat.getColor(this, R.color.primary2)
        window.navigationBarColor = formColor

        // 🔹 Supaya ikon navigation bar tetap terlihat jelas (misalnya hitam di atas warna terang)
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        // 🔹 Terapkan padding agar tidak bentrok dengan status bar & navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val tv_sign_in = findViewById<TextView>(R.id.tv_sign_in)

        tv_sign_in.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
        setupPasswordToggle()

        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ) {
                Toast.makeText(this@RegisterActivity, "Kolom inputan tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // Optional: Validasi
            if (password != confirmPassword) {
                Toast.makeText(this, "Password dan konfirmasi tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val user = User(name, email, password, confirmPassword)

            val userService = RetrofitInstance.getInstance(this).create(APIServices::class.java)
            userService.registerUser(user).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful && response.body() != null) {
                        val intent = Intent(this@RegisterActivity, otpRegisVerificationActivity::class.java)
                        Toast.makeText(this@RegisterActivity, "Berhasil", Toast.LENGTH_LONG).show()
                        intent.putExtra("email", email) // kirim email yang baru didaftarkan
                        startActivity(intent)
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("RegisterError", "Gagal Register: $errorBody")
                        Toast.makeText(this@RegisterActivity, "Gagal Register: $errorBody", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("RegisterError", "Throwable: ${t.message}")
                }
            })
        }
    }
    private fun setupPasswordToggle() {
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val confirmPasswordEditText = findViewById<EditText>(R.id.editTextConfirmPassword)

        passwordEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // index for drawableEnd
                val drawable = passwordEditText.compoundDrawables[drawableEnd]
                if (drawable != null && event.rawX >= (passwordEditText.right - drawable.bounds.width())) {
                    isPasswordVisible = !isPasswordVisible
                    togglePasswordVisibility(passwordEditText, isPasswordVisible)
                    return@setOnTouchListener true
                }
            }
            false
        }

        confirmPasswordEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                val drawable = confirmPasswordEditText.compoundDrawables[drawableEnd]
                if (drawable != null && event.rawX >= (confirmPasswordEditText.right - drawable.bounds.width())) {
                    isConfirmPasswordVisible = !isConfirmPasswordVisible
                    togglePasswordVisibility(confirmPasswordEditText, isConfirmPasswordVisible)
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun togglePasswordVisibility(editText: EditText, isVisible: Boolean) {
        if (isVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_invisible, 0)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_invisible, 0)
        }
        editText.setSelection(editText.text.length) // cursor tetap di akhir teks
    }
}
