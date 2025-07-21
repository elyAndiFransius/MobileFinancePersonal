package com.example.personalfinancemobile.activity.Auth

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextWatcher
import android.text.Editable
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalfinancemobile.R
import com.example.personalfinancemobile.activity.Auth.otpRegisVerificationActivity
import com.example.personalfinancemobile.app.data.model.OTPResponse
import com.example.personalfinancemobile.app.data.model.ResetResponse
import com.example.personalfinancemobile.app.data.model.SentOTPResponse
import com.example.personalfinancemobile.app.data.model.Validasi
import com.example.personalfinancemobile.app.data.model.resetPasswordOpt
import com.example.personalfinancemobile.app.data.model.sentOTP
import com.example.personalfinancemobile.app.data.network.APIServices
import com.example.personalfinancemobile.app.data.network.RetrofitInstance
import com.example.personalfinancemobile.databinding.ActivityOtpResetVerificationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class otpResetVerificationActivity : AppCompatActivity() {
    private lateinit var timer: CountDownTimer
    private lateinit var timerText: TextView
    private lateinit var binding: ActivityOtpResetVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // INISIALISASI BINDING DULU
        binding = ActivityOtpResetVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root) // JANGAN pakai setContentView(R.layout....)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        timerText = binding.timerText
        setupOtpInputs()
        startCountdownTimer()

        val btnSave = binding.btnSave
        val reset = binding.reset

        btnSave.setOnClickListener {

            val kodeOtp = listOf(
                binding.otp1.text.toString(),
                binding.otp2.text.toString(),
                binding.otp3.text.toString(),
                binding.otp4.text.toString(),
                binding.otp5.text.toString(),
                binding.otp6.text.toString()
            ).joinToString("")

            if (kodeOtp.length < 6) {
                Toast.makeText(this, "Kode OTP belum lengkap", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val email = intent.getStringExtra("email") ?: return@setOnClickListener
            val data = resetPasswordOpt(email, kodeOtp)
            val userService = RetrofitInstance.getInstance(this).create(APIServices::class.java)

            userService.verifyOtpForReset(data).enqueue(object : Callback<ResetResponse> {
                override fun onResponse(call: Call<ResetResponse>, response: Response<ResetResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(this@otpResetVerificationActivity, "Verifikasi berhasil", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@otpResetVerificationActivity, LoginActivity::class.java))
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("otpResetError", "Gagal Reset Password: $errorBody")
                        Toast.makeText(this@otpResetVerificationActivity, "Gagal", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResetResponse>, t: Throwable) {
                    Toast.makeText(this@otpResetVerificationActivity, "Gagal koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        reset.setOnClickListener {
            val email = intent.getStringExtra("email") ?: return@setOnClickListener
            val data = sentOTP(email)
            val userService = RetrofitInstance.getInstance(this).create(APIServices::class.java)

            userService.sendOTP(data).enqueue(object : Callback<SentOTPResponse> {
                override fun onResponse(call: Call<SentOTPResponse>, response: Response<SentOTPResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(this@otpResetVerificationActivity, "Kode baru telah dikirim", Toast.LENGTH_SHORT).show()

                        // Reset semua field OTP
                        binding.otp1.setText("")
                        binding.otp2.setText("")
                        binding.otp3.setText("")
                        binding.otp4.setText("")
                        binding.otp5.setText("")
                        binding.otp6.setText("")
                        binding.otp1.requestFocus()

                        startCountdownTimer()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("OtpError", "OtpError: $errorBody")
                        Toast.makeText(this@otpResetVerificationActivity, response.body()?.message ?: "Tunggu 30 detik!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SentOTPResponse>, t: Throwable) {
                    Toast.makeText(this@otpResetVerificationActivity, "Gagal koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun setupOtpInputs() {
        val otpFields = listOf(
            binding.otp1,
            binding.otp2,
            binding.otp3,
            binding.otp4,
            binding.otp5,
            binding.otp6
        )
        for (i in otpFields.indices) {
            otpFields[i].addTextChangedListener(object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: android.text.Editable?) {
                    if (s?.length == 1 && i < otpFields.size - 1) {
                        otpFields[i + 1].requestFocus()
                    } else if (s?.isEmpty() == true && i > 0) {
                        otpFields[i - 1].requestFocus()
                    }
                }
            })
        }
    }


    private fun startCountdownTimer() {
        timer = object : CountDownTimer(5 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerText.text = String.format("%02d:%02d", minutes, seconds)
            }
            override fun onFinish() {
                timerText.text = "00:00"
            }
        }.start()
    }
}