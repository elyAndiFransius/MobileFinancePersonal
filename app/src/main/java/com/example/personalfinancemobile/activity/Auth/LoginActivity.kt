package com.example.personalfinancemobile.activity.Auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.MainActivity
import com.example.personalfinancemobile.app.data.model.Auth.loginRequest
import com.example.personalfinancemobile.app.data.model.Auth.loginResponse
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.app.ui.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
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
        sessionManager = SessionManager(this)

        // Jika user sudah login dan token tersedia, langsung ke main activity
        if (sessionManager.isLoggedIn()) {
            navigateToMainActivity()
            return
        }
        setupPasswordToggle()

        // Setup UI components
        val login = findViewById<Button>(R.id.btn_login)
        val register = findViewById<TextView>(R.id.tv_sign_up)
        val resetPassword = findViewById<TextView>(R.id.resetPassword)

        resetPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email = findViewById<EditText>(R.id.editTextEmail)
        val password = findViewById<EditText>(R.id.editTextPassword)

        val emailString = email.text.toString().trim()
        val passwordString = password.text.toString().trim()

        // Validasi input
        if (emailString.isEmpty() || passwordString.isEmpty()) {
            Toast.makeText(this@LoginActivity, "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        // Validasi format email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            Toast.makeText(this@LoginActivity, "Format email tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        val request = loginRequest(emailString, passwordString)

        // Disable button saat proses login
        val loginButton = findViewById<Button>(R.id.btn_login)
        loginButton.isEnabled = false
        loginButton.text = "Loading..."

        // API Call
        val userService = RetrofitInstance.getInstance(this).create(APIServices::class.java)
        userService.login(request).enqueue(object : Callback<loginResponse> {
            override fun onResponse(call: Call<loginResponse>, response: Response<loginResponse>) {
                // Re-enable button
                loginButton.isEnabled = true
                loginButton.text = "Login"

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val token = loginResponse?.access_token

                    if (!token.isNullOrEmpty() && loginResponse?.user != null) {

                        val user = loginResponse.user
                        // Simpan token dan status login
                        saveLoginData(
                            token,
                            user.id.toString(),
                            user.name ?: "",
                            user.email ?: ""
                        )
                        Toast.makeText(this@LoginActivity, "Berhasil Login", Toast.LENGTH_SHORT).show()
                        Log.d("LoginActivity", "Login berhasil, token disimpan")

                        navigateToMainActivity()
                    } else {
                        Log.e("LoginActivity", "Token kosong dari response")
                        Toast.makeText(this@LoginActivity, "Token tidak valid", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        401 -> "Email atau password salah"
                        404 -> "User tidak ditemukan"
                        500 -> "Server error, coba lagi nanti"
                        else -> "Login gagal (${response.code()})"
                    }
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Login gagal: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                // Re-enable button
                loginButton.isEnabled = true
                loginButton.text = "Login"

                Toast.makeText(this@LoginActivity, "Koneksi bermasalah: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("LoginActivity", "Network error: ${t.message}")
            }
        })
    }

    private fun saveLoginData(token: String, userId: String, userName: String, userEmail : String) {
        val sessionManager = SessionManager(this)

        sessionManager.saveAuthToken(token)
        sessionManager.saveUserData(userId, userName, userEmail)


        Log.d("LoginActivity", "Token dan status login berhasil disimpan: $token, $userId, $userEmail")
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish() // Tutup LoginActivity agar user tidak bisa kembali dengan tombol back
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

    // Method untuk logout (panggil dari activity lain jika diperlukan)
    companion object {
        fun logout(context: Context) {
            val sessionManager = SessionManager(context)
            sessionManager.logout()

            // Navigate ke login activity
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}