package com.example.personalfinancemobile.activity.Auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.User
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val tv_sign_up = findViewById<TextView>(R.id.tv_sign_up)

        tv_sign_up.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()



            // Optional: Validasi sederhana
            if (password != confirmPassword) {
                Toast.makeText(this, "Password dan konfirmasi tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(name, email, password, confirmPassword)

            val userService = RetrofitInstance.instance.create(APIServices::class.java)
            userService.registerUser(user).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(this@RegisterActivity, "Register berhasil!", Toast.LENGTH_SHORT).show()
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
}
