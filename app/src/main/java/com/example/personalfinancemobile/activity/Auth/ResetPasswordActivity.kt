package com.example.personalfinancemobile.activity.Auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.app.data.model.ResetResponse
import com.example.personalfinancemobile.app.data.model.resetPassword
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextConfirmPassword = findViewById<EditText>(R.id.editPasswordComfirm)
        val btnnReset = findViewById<AppCompatButton>(R.id.btnSimpan)


        btnnReset.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            if (password != confirmPassword) {
                Toast.makeText(this, "Password dan konfirmasi tidak sama", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val reset = resetPassword(email, password)

            val userService = RetrofitInstance.getInstance(this).create(APIServices::class.java)
            userService.sendOtpForReset(reset).enqueue(object : Callback<ResetResponse> {
                override fun onResponse(
                    call: Call<ResetResponse>,
                    response: Response<ResetResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val intent = Intent(this@ResetPasswordActivity, otpResetVerificationActivity::class.java)
                        Toast.makeText(this@ResetPasswordActivity, "Berhasil", Toast.LENGTH_LONG).show()
                        intent.putExtra("email", email)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("ResetError", "Gagal Reset Password: $errorBody")
                        Toast.makeText(
                            this@ResetPasswordActivity,
                            "Gagal Reset password: $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResetResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("ResetError", "Throwable: ${t.message}")
                }
            })
        }
    }
}